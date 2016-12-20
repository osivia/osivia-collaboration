package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AclEntry;
import org.osivia.services.workspace.portlet.model.Record;
import org.osivia.services.workspace.portlet.model.RecordType;
import org.osivia.services.workspace.portlet.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Workspace ACL management repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AclManagementRepository
 */
@Repository
public class AclManagementRepositoryImpl implements AclManagementRepository {

    /** Path window property. */
    private static final String PATH_WINDOW_PROPERTY = Constants.WINDOW_PROP_URI;


    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public AclManagementRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setForcePublicationInfosScope("superuser_context");

        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        // Path window property
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS service context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Workspace Nuxeo document
        Document workspace = null;
        try {
            while ((workspace == null) && StringUtils.isNotEmpty(path)) {
                // Publication infos
                CMSPublicationInfos publicationInfos = cmsService.getPublicationInfos(cmsContext, path);
                // Base path
                String basePath = publicationInfos.getPublishSpacePath();
                // Space config
                CMSItem spaceConfig = cmsService.getSpaceConfig(cmsContext, basePath);
                // Document type
                DocumentType documentType = spaceConfig.getType();

                if ((documentType != null) && ("Workspace".equals(documentType.getName()))) {
                    workspace = (Document) spaceConfig.getNativeItem();
                } else {
                    // Loop on parent path
                    path = publicationInfos.getParentSpaceID();
                }
            }
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        return workspace.getString("webc:url");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AclEntries getAclEntries(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Current user
        String user = request.getRemoteUser();

        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        // Path window property
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        // ACL entries
        AclEntries entries = new AclEntries();
        entries.setWorkspaceId(workspaceId);

        if (path == null) {
            entries.setEntries(new ArrayList<AclEntry>(0));
        } else {
            // Nuxeo document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path, true);
            // Nuxeo document
            Document document = documentContext.getDoc();
            entries.setDocument(document);

            // Nuxeo command
            INuxeoCommand command = new GetPermissionsCommand(document);
            JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(command);

            if (result == null) {
                entries.setEntries(new ArrayList<AclEntry>(0));
            } else {
                // JSON array
                JSONArray array = result.getJSONArray("local");


                // Workspace groups
                List<CollabProfile> groups = this.workspaceService.findByWorkspaceId(workspaceId);
                Map<String, CollabProfile> groupMap = new HashMap<>(groups.size());
                for (CollabProfile group : groups) {
                    groupMap.put(group.getCn(), group);
                }

                // Workspace members
                List<WorkspaceMember> members = this.workspaceService.getAllMembers(workspaceId);
                Map<String, WorkspaceMember> memberMap = new HashMap<>(members.size());
                for (WorkspaceMember member : members) {
                    Person person = member.getMember();
                    memberMap.put(person.getUid(), member);
                }


                // Permissions
                Map<String, Permission> permissions = new HashMap<>(array.size());
                boolean inherited = true;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    if (object.getBoolean("isGranted")) {
                        // Name
                        String name = object.getString("username");
                        // Group indicator
                        boolean group = object.getBoolean("isGroup");

                        // Permission
                        Permission permission = permissions.get(name);
                        if (permission == null) {
                            permission = new Permission();
                            permission.setName(name);
                            permission.setValues(new ArrayList<String>());
                            permission.setGroup(group);
                            permissions.put(name, permission);
                        }
                        permission.getValues().add(object.getString("permission"));
                    } else {
                        inherited = false;
                    }
                }
                entries.setInherited(inherited);

                entries.setEntries(new ArrayList<AclEntry>(permissions.size()));
                for (Permission permission : permissions.values()) {
                    // Permission values
                    String[] values = permission.getValues().toArray(new String[permission.getValues().size()]);
                    // Workspace role
                    WorkspaceRole workspaceRole = WorkspaceRole.fromPermissions(values);
                    if (workspaceRole == null) {
                        // Unknown workspace role
                        continue;
                    }
                    // Role
                    Role role = new Role(workspaceRole);
                    role.setDisplayName(bundle.getString(workspaceRole.getKey(), workspaceRole.getClassLoader()));
                    // Identifier
                    String id = permission.getName();
                    // Type
                    RecordType type;
                    // Display name
                    String displayName;
                    // Avatar
                    String avatar = null;
                    // Extra informations
                    String extra;
                    // Editable ACL entry indicator
                    boolean editable;

                    if (permission.isGroup()) {
                        // Group
                        type = RecordType.GROUP;

                        CollabProfile group = groupMap.get(id);
                        if (WorkspaceGroupType.space_group.equals(group.getType())) {
                            displayName = bundle.getString("ALL");
                            extra = bundle.getString("ALL_WORKSPACE_MEMBERS");
                        } else if (WorkspaceGroupType.local_group.equals(group.getType())) {
                            displayName = group.getDisplayName();
                            extra = group.getDescription();
                        } else {
                            // Hide other workspace groups
                            continue;
                        }

                        editable = true;
                    } else {
                        // User
                        type = RecordType.USER;

                        WorkspaceMember member = memberMap.get(id);
                        if (member == null) {
                            // Hide non workspace member
                            continue;
                        } else {
                            Person person = member.getMember();
                            displayName = StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid());
                            avatar = person.getAvatar().getUrl();
                            extra = person.getMail();
                        }

                        editable = entries.isInherited() || !StringUtils.equals(user, id);
                    }

                    // ACL entry
                    AclEntry entry = new AclEntry();
                    entry.setId(id);
                    entry.setType(type);
                    entry.setDisplayName(displayName);
                    entry.setAvatar(avatar);
                    entry.setExtra(extra);
                    entry.setRole(role);
                    entry.setEditable(editable);

                    entries.getEntries().add(entry);
                }
            }
        }

        return entries;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> getRoles(PortalControllerContext portalControllerContext) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace roles
        WorkspaceRole[] workspaceRoles = WorkspaceRole.values();

        // Roles
        List<Role> roles = new ArrayList<>(workspaceRoles.length);
        for (WorkspaceRole workspaceRole : workspaceRoles) {
            if (!WorkspaceRole.OWNER.equals(workspaceRole)) {
                Role role = new Role(workspaceRole);
                role.setDisplayName(bundle.getString(workspaceRole.getKey(), workspaceRole.getClassLoader()));
                roles.add(role);
            }
        }

        return roles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Role getDefaultRole(PortalControllerContext portalControllerContext) throws PortletException {
        return new Role(WorkspaceRole.READER);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Record> getGroupRecords(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace groups
        List<CollabProfile> groups = this.workspaceService.findByWorkspaceId(workspaceId);

        // Records
        List<Record> records;
        if (groups == null) {
            records = new ArrayList<>(0);
        } else {
            records = new ArrayList<>(groups.size());
            for (CollabProfile group : groups) {
                WorkspaceGroupType type = group.getType();

                if (WorkspaceGroupType.space_group.equals(type) || WorkspaceGroupType.local_group.equals(type)) {
                    Record record = new Record();
                    record.setId(group.getCn());
                    record.setType(RecordType.GROUP);

                    // Display name & extra informations
                    String displayName;
                    String extra;
                    if (WorkspaceGroupType.space_group.equals(type)) {
                        displayName = bundle.getString("ALL");
                        extra = bundle.getString("ALL_WORKSPACE_MEMBERS");
                    } else {
                        displayName = StringUtils.defaultIfBlank(group.getDisplayName(), group.getCn());
                        extra = group.getDescription();
                    }
                    record.setDisplayName(displayName);
                    record.setExtra(extra);

                    records.add(record);
                }
            }
        }

        return records;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Record> getUserRecords(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Workspace members
        List<WorkspaceMember> members = this.workspaceService.getAllMembers(workspaceId);

        // Records
        List<Record> records;
        if (members == null) {
            records = new ArrayList<>(0);
        } else {
            records = new ArrayList<>(members.size());
            for (WorkspaceMember member : members) {
                // Person
                Person person = member.getMember();

                // Record
                Record record = new Record();
                record.setId(person.getUid());
                record.setType(RecordType.USER);
                record.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid()));

                // Avatar
                record.setAvatar(person.getAvatar().getUrl());

                // Extra informations
                record.setExtra(person.getMail());

                records.add(record);
            }
        }

        return records;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changeInheritance(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Document
        Document document = entries.getDocument();

        // Permissions 
        List<Permission> permissions = new ArrayList<Permission>();
        // Build from model
        if(entries != null && CollectionUtils.isNotEmpty(entries.getEntries())){
            for(AclEntry aclEntry : entries.getEntries()){
                Permission permission = PermissionsAdapter.buildPermission(aclEntry);
                permissions.add(permission);
            }
        }
        
        // Update permissions blocking inheritance or not
        INuxeoCommand command = new AddPermissionsCommand(document, permissions, PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, !entries.isInherited());
        nuxeoController.executeNuxeoCommand(command);
        
        // Update model:
        // Fetch of ACLs cause there are implicit inheritance rules managed by back office
        AclEntries updatedEntries = this.getAclEntries(portalControllerContext, entries.getWorkspaceId());
        entries.setEntries(updatedEntries.getEntries());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PortalControllerContext portalControllerContext, AclEntries entries, List<Role> roles) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Document
        Document document = entries.getDocument();


        // Updated Permissions
        List<Permission> updatedPermissions = new ArrayList<Permission>();
        // Removed Permissions by user
        List<String> users = new ArrayList<String>();
        // Removed ACL entries
        List<AclEntry> removedEntries = new ArrayList<AclEntry>();

        for (AclEntry entry : entries.getEntries()) {
            if (entry.isDeleted()) {
                // User whose remove permissions
                users.add(entry.getId());
                
                // AclEntry
                removedEntries.add(entry);
            } else if (entry.isUpdated()) {
                // Updated permission to add
                Permission permission = PermissionsAdapter.buildPermission(entry);
                updatedPermissions.add(permission);
                
                // User whose remove permissions
                users.add(entry.getId());
                
                // AclEntry
                entry.setUpdated(false);
            }
        }


        // Update: remove permissions by users and add permissions:
        
        // Remove permissions
        if (!updatedPermissions.isEmpty() || !users.isEmpty()) {
            // Nuxeo command
            INuxeoCommand command = new RemovePermissionsCommand(document, PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, users,  
                    !entries.isInherited());
            nuxeoController.executeNuxeoCommand(command);
        }

        // Add permissions
        if (!updatedPermissions.isEmpty()) {
            // Nuxeo command
            INuxeoCommand command = new AddPermissionsCommand(document, updatedPermissions, 
                    PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, !entries.isInherited());
            nuxeoController.executeNuxeoCommand(command);
        }


        // Update model
        entries.getEntries().removeAll(removedEntries);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PortalControllerContext portalControllerContext, AclEntries entries, List<Record> records, Role role) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Selected workspace role
        WorkspaceRole selectedWorkspaceRole = null;
        if (role != null) {
            selectedWorkspaceRole = WorkspaceRole.fromId(role.getId());
        }

        if (CollectionUtils.isNotEmpty(records) && (selectedWorkspaceRole != null)) {
            // Added Permissions
            List<Permission> addedPermissions = new ArrayList<Permission>(records.size());
            // RemovedPermissions
            List<Permission> removedPermissions = new ArrayList<Permission>(records.size());

            // Added ACL entries
            List<AclEntry> addedEntries = new ArrayList<>(records.size());
            // Removed ACL entries
            List<AclEntry> removedEntries = new ArrayList<>();

            for (Record record : records) {
                int index = entries.getEntries().indexOf(record);
                if (index != -1) {
                    AclEntry entry = entries.getEntries().get(index);
                    if (role.getWeight() > entry.getRole().getWeight()) {
                        // Replace ACL entry
                        removedEntries.add(entry);
                        
                        // Replace permission
                        Permission permission = new Permission();
                        permission.setName(entry.getId());
                        permission.setValues(Arrays.asList(selectedWorkspaceRole.getPermissions()));
                        removedPermissions.add(permission);
                    } else {
                        // Ignore record
                        continue;
                    }
                }

                Permission permission = new Permission();
                permission.setName(record.getId());
                permission.setValues(Arrays.asList(selectedWorkspaceRole.getPermissions()));
                addedPermissions.add(permission);

                // Added ACL entry
                AclEntry addedEntry = new AclEntry();
                addedEntry.setId(record.getId());
                addedEntry.setType(record.getType());
                addedEntry.setDisplayName(StringUtils.defaultIfBlank(record.getDisplayName(), record.getId()));
                addedEntry.setAvatar(record.getAvatar());
                addedEntry.setExtra(record.getExtra());
                addedEntry.setRole(role);
                addedEntry.setEditable(true);

                addedEntries.add(addedEntry);
            }


            // Remove permissions
            if (!removedEntries.isEmpty()) {
                INuxeoCommand command = new RemovePermissionsCommand(entries.getDocument(), removedPermissions, 
                        PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, !entries.isInherited());
                nuxeoController.executeNuxeoCommand(command);
            }

            // Add permissions
            if (!addedPermissions.isEmpty()) {
                INuxeoCommand command = new AddPermissionsCommand(entries.getDocument(), addedPermissions, 
                        PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, !entries.isInherited());
                nuxeoController.executeNuxeoCommand(command);
            }


            // Update model
            entries.getEntries().removeAll(removedEntries);
            entries.getEntries().addAll(addedEntries);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Document
        Document document = entries.getDocument();
        
        // Remove local ACL non blocking inheritance
        INuxeoCommand removeGroupPermissionsCommand = new RemovePermissionsCommand(document, PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, false);
        nuxeoController.executeNuxeoCommand(removeGroupPermissionsCommand);


        // Update ACL entries
        entries.setInherited(true);
        AclEntries updatedEntries = this.getAclEntries(portalControllerContext, entries.getWorkspaceId());
        entries.setEntries(updatedEntries.getEntries());
    }
    
}

