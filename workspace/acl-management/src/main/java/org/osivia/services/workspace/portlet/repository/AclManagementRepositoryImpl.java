package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
                if (array.isEmpty()) {
                    array = result.getJSONArray("inherited");
                    entries.setInherited(true);
                }


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
                    }
                }
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
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Document
        Document document = entries.getDocument();
        
        // Current user
        String user = request.getRemoteUser();
        // Current workspace member
        WorkspaceMember member = this.workspaceService.getMember(entries.getWorkspaceId(), user);

        // Nuxeo command
        INuxeoCommand command;

        if (entries.isInherited()) {
            // Permission names
            List<String> names = new ArrayList<>();
            names.add(Permission.getInheritanceBlocking().getName());
            if (member != null) {
                names.add(user);
            }

            // Remove permissions
            command = new RemovePermissionsCommand(document, names);

            // Update model
            if (member != null) {
                // Removed entries
                List<AclEntry> removedEntries = new ArrayList<>();
                for (AclEntry entry : entries.getEntries()) {
                    if (StringUtils.equals(user, entry.getId())) {
                        removedEntries.add(entry);
                    }
                }
                entries.getEntries().removeAll(removedEntries);
            }
        } else {
            // Permissions
            List<Permission> permissions = new ArrayList<>();

            if (member != null) {
                // Current user administration permission
                Permission userPermission = new Permission();
                userPermission.setName(user);
                userPermission.setValues(Arrays.asList(WorkspaceRole.ADMIN.getPermissions()));

                permissions.add(userPermission);
            }

            permissions.add(Permission.getInheritanceBlocking());

            // Add permissions
            command = new AddPermissionsCommand(document, permissions);


            // Update model
            if (member != null) {
                // Remove previous ACL entries
                List<AclEntry> removedEntries = new ArrayList<>();
                for (AclEntry entry : entries.getEntries()) {
                    if (StringUtils.equals(user, entry.getId())) {
                        removedEntries.add(entry);
                    }
                }
                entries.getEntries().removeAll(removedEntries);
                
                // Current person
                Person person = member.getMember();

                // Current role
                Role role = new Role(WorkspaceRole.ADMIN);
                role.setDisplayName(bundle.getString(WorkspaceRole.ADMIN.getKey(), WorkspaceRole.ADMIN.getClassLoader()));

                // ACL entry
                AclEntry entry = new AclEntry();
                entry.setId(user);
                entry.setType(RecordType.USER);
                entry.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), user));
                entry.setAvatar(person.getAvatar().getUrl());
                entry.setExtra(person.getMail());
                entry.setRole(role);
                entry.setEditable(false);

                entries.getEntries().add(entry);
            }
        }

        nuxeoController.executeNuxeoCommand(command);
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


        // Updated ACL entries
        List<AclEntry> updatedEntries = new ArrayList<>();
        // Removed ACL entries
        List<AclEntry> removedEntries = new ArrayList<>();

        for (AclEntry entry : entries.getEntries()) {
            if (entry.isDeleted()) {
                removedEntries.add(entry);
            } else if (entry.isUpdated()) {
                updatedEntries.add(entry);

                entry.setUpdated(false);
            }
        }


        // Remove permissions
        if (!updatedEntries.isEmpty() || !removedEntries.isEmpty()) {
            List<String> names = new ArrayList<>(updatedEntries.size() + removedEntries.size());
            for (AclEntry entry : updatedEntries) {
                names.add(entry.getId());
            }
            for (AclEntry entry : removedEntries) {
                names.add(entry.getId());
            }

            // Nuxeo command
            INuxeoCommand command = new RemovePermissionsCommand(document, names);
            nuxeoController.executeNuxeoCommand(command);
        }


        // Add permissions
        if (!updatedEntries.isEmpty()) {
            List<Permission> permissions = new ArrayList<>(updatedEntries.size());

            for (AclEntry entry : updatedEntries) {
                // Workspace role
                WorkspaceRole workspaceRole = WorkspaceRole.fromId(entry.getRole().getId());
                if (workspaceRole != null) {
                    Permission permission = new Permission();
                    permission.setName(entry.getId());
                    permission.setValues(Arrays.asList(workspaceRole.getPermissions()));
                    permissions.add(permission);
                }
            }

            // Nuxeo command
            INuxeoCommand command = new AddPermissionsCommand(document, permissions);
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
            // Permissions
            List<Permission> permissions = new ArrayList<>(records.size());

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
                    } else {
                        // Ignore record
                        continue;
                    }
                }

                Permission permission = new Permission();
                permission.setName(record.getId());
                permission.setValues(Arrays.asList(selectedWorkspaceRole.getPermissions()));
                permissions.add(permission);

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
                List<String> names = new ArrayList<>(removedEntries.size());
                for (AclEntry entry : removedEntries) {
                    names.add(entry.getId());
                }

                INuxeoCommand command = new RemovePermissionsCommand(entries.getDocument(), names);
                nuxeoController.executeNuxeoCommand(command);
            }

            // Add permissions
            if (!permissions.isEmpty()) {
                INuxeoCommand command = new AddPermissionsCommand(entries.getDocument(), permissions);
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

        // Get permissions Nuxeo command
        INuxeoCommand getPermissionsCommand = new GetPermissionsCommand(document);
        JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(getPermissionsCommand);

        // Local permissions JSON array
        JSONArray array = result.getJSONArray("local");
        List<String> names = new ArrayList<>(array.size());

        if (!entries.isInherited()) {
            Permission permission = Permission.getInheritanceBlocking();
            names.add(permission.getName());
        }

        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);

            if (object.getBoolean("isGranted")) {
                names.add(object.getString("username"));
            }
        }

        // Remove permissions Nuxeo command
        INuxeoCommand removePermissionsCommand = new RemovePermissionsCommand(document, names);
        nuxeoController.executeNuxeoCommand(removePermissionsCommand);


        // Update ACL entries
        entries.setInherited(true);
        AclEntries updatedEntries = this.getAclEntries(portalControllerContext, entries.getWorkspaceId());
        entries.setEntries(updatedEntries.getEntries());
    }

}

