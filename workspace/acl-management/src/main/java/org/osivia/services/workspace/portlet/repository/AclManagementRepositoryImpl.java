package org.osivia.services.workspace.portlet.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.naming.Name;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.beanutils.BeanUtils;
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
import org.osivia.services.workspace.portlet.model.Permission;
import org.osivia.services.workspace.portlet.model.Record;
import org.osivia.services.workspace.portlet.model.RecordType;
import org.osivia.services.workspace.portlet.model.Role;
import org.osivia.services.workspace.portlet.model.SynthesisNode;
import org.osivia.services.workspace.portlet.model.SynthesisNodeType;
import org.osivia.services.workspace.portlet.repository.command.AddPermissionsCommand;
import org.osivia.services.workspace.portlet.repository.command.GetPermissionsCommand;
import org.osivia.services.workspace.portlet.repository.command.RemovePermissionsCommand;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
 * @see ApplicationContextAware
 */
@Repository
public class AclManagementRepositoryImpl implements AclManagementRepository, ApplicationContextAware {

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


    /** Application context. */
    private ApplicationContext applicationContext;


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
        AclEntries entries = this.applicationContext.getBean(AclEntries.class);
        entries.setWorkspaceId(workspaceId);

        if (path == null) {
            entries.setEntries(new ArrayList<AclEntry>(0));
        } else {
            // Nuxeo document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
            documentContext.reload();
            // Nuxeo document
            Document document = documentContext.getDocument();
            entries.setDocument(document);

            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetPermissionsCommand.class, document);
            JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(command);

            if (result == null) {
                entries.setEntries(new ArrayList<AclEntry>(0));
            } else {
                // JSON local array
                JSONArray localArray = result.getJSONArray("local");
                // JSON inherited array
                JSONArray inheritedArray = result.getJSONArray("inherited");


                // Public inheritance
                boolean publicInheritance = false;
                for (int i = 0; i < inheritedArray.size(); i++) {
                    JSONObject object = inheritedArray.getJSONObject(i);

                    if (object.getBoolean("isGranted")) {
                        // Name
                        String name = object.getString("username");

                        if (Permission.PUBLIC_NAME.equals(name)) {
                            publicInheritance = true;
                        }
                    }
                }
                entries.setPublicInheritance(publicInheritance);


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
                Map<String, Permission> permissions = new HashMap<>(localArray.size());
                boolean publicEntry = false;
                boolean inherited = true;
                for (int i = 0; i < localArray.size(); i++) {
                    JSONObject object = localArray.getJSONObject(i);

                    if (object.getBoolean("isGranted")) {
                        // Name
                        String name = object.getString("username");

                        if (Permission.PUBLIC_NAME.equals(name)) {
                            publicEntry = true;
                        } else {
                            // Group indicator
                            boolean group = object.getBoolean("isGroup");

                            // Permission
                            Permission permission = permissions.get(name);
                            if (permission == null) {
                                permission = this.applicationContext.getBean(Permission.class);
                                permission.setName(name);
                                permission.setValues(new ArrayList<String>());
                                permission.setGroup(group);
                                permissions.put(name, permission);
                            }
                            permission.getValues().add(object.getString("permission"));
                        }
                    } else {
                        inherited = false;
                    }
                }
                entries.setPublicEntry(publicEntry);
                entries.setPublicEntryOriginal(publicEntry);
                entries.setInherited(inherited);
                entries.setInheritedOriginal(inherited);

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
                    Role role = this.applicationContext.getBean(Role.class, workspaceRole);
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
                        if(group != null) {
                            if (WorkspaceGroupType.space_group.equals(group.getType())) {
                                displayName = bundle.getString("ALL");
                                extra = bundle.getString("ALL_WORKSPACE_MEMBERS");
                            } else if (WorkspaceGroupType.local_group.equals(group.getType())) {
                                displayName = group.getDisplayName();
                                extra = group.getDescription();
                            }
                            else {
                                // Hide other workspace groups
                                continue;
                            }
                        } else {
                            // Hide other groups
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
                    AclEntry entry = this.applicationContext.getBean(AclEntry.class);
                    entry.setId(id);
                    entry.setType(type);
                    entry.setDisplayName(displayName);
                    entry.setAvatar(avatar);
                    entry.setExtra(extra);
                    entry.setRole(role);
                    entry.setEditable(editable);

                    entries.getEntries().add(entry);
                }

                entries.setModified(publicEntry || !inherited || !entries.getEntries().isEmpty());
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
                Role role = this.applicationContext.getBean(Role.class, workspaceRole);
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
        return this.applicationContext.getBean(Role.class, WorkspaceRole.READER);
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
                    Record record = this.applicationContext.getBean(AclEntry.class);
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
                Record record = this.applicationContext.getBean(AclEntry.class);
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
        List<Permission> removedEntries = new ArrayList<Permission>(1);

        for (AclEntry entry : entries.getEntries()) {
            if (entry.isDeleted()) {
                // User whose remove permissions
                users.add(entry.getId());
            } else if (entry.isUpdated()) {
                // Updated permission
                Permission permission = this.toPermission(entry);
                updatedPermissions.add(permission);
                
                // Clean user permissions
                users.add(entry.getId());
                
                // AclEntry
                entry.setUpdated(false);
            }
        }

        
        // Public permission
        Permission publicPermission = this.applicationContext.getBean(Permission.class);
        publicPermission.setName(Permission.PUBLIC_NAME);
        publicPermission.setValues(Arrays.asList(WorkspaceRole.READER.getPermissions()));

        if (entries.isPublicEntry() && !entries.isPublicEntryOriginal()) {
            updatedPermissions.add(publicPermission);
        } else if (!entries.isPublicEntry() && entries.isPublicEntryOriginal()) {
            removedEntries.add(publicPermission);
        }

        
        // Remove permissions
        if (!removedEntries.isEmpty() || !users.isEmpty()) {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(RemovePermissionsCommand.class, document, removedEntries, entries.isInherited(), users);
            nuxeoController.executeNuxeoCommand(command);
        }

        // Add permissions
        if (!updatedPermissions.isEmpty() || (entries.isInherited() != entries.isInheritedOriginal())) {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(AddPermissionsCommand.class, document, updatedPermissions, entries.isInherited());
            nuxeoController.executeNuxeoCommand(command);
        }


        // Update model
        this.updateModel(portalControllerContext, entries);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelUpdate(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        this.updateModel(portalControllerContext, entries);
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
                        Permission permission = this.applicationContext.getBean(Permission.class);
                        permission.setName(entry.getId());
                        permission.setValues(Arrays.asList(selectedWorkspaceRole.getPermissions()));
                        removedPermissions.add(permission);
                    } else {
                        // Ignore record
                        continue;
                    }
                }

                Permission permission = this.applicationContext.getBean(Permission.class);
                permission.setName(record.getId());
                permission.setValues(Arrays.asList(selectedWorkspaceRole.getPermissions()));
                addedPermissions.add(permission);

                // Added ACL entry
                AclEntry addedEntry = this.applicationContext.getBean(AclEntry.class);
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
                INuxeoCommand command = this.applicationContext.getBean(RemovePermissionsCommand.class, entries.getDocument(), removedPermissions,
                        entries.isInherited());
                nuxeoController.executeNuxeoCommand(command);
            }

            // Add permissions
            if (!addedPermissions.isEmpty()) {
                INuxeoCommand command = this.applicationContext.getBean(AddPermissionsCommand.class, entries.getDocument(), addedPermissions,
                        entries.isInherited());
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
        INuxeoCommand removeGroupPermissionsCommand = this.applicationContext.getBean(RemovePermissionsCommand.class, document);
        nuxeoController.executeNuxeoCommand(removeGroupPermissionsCommand);


        // Update ACL entries
        this.updateModel(portalControllerContext, entries);
    }

    
    /**
     * Transform ACL entry to permission.
     * 
     * @param entry ACL entry
     * @return permission
     */
    private Permission toPermission(AclEntry entry) {
        // Workspace role
        WorkspaceRole workspaceRole = WorkspaceRole.fromId(entry.getRole().getId());

        // Permission
        Permission permission = this.applicationContext.getBean(Permission.class);
        permission.setName(entry.getId());
        permission.setValues(Arrays.asList(workspaceRole.getPermissions()));

        return permission;
    }


    /**
     * Update model.
     * 
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @throws PortletException
     */
    private void updateModel(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        AclEntries updatedEntries = this.getAclEntries(portalControllerContext, entries.getWorkspaceId());
        try {
            BeanUtils.copyProperties(entries, updatedEntries);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SortedSet<SynthesisNode> getSynthesisNodes(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        // Path window property
        String path = window.getProperty(PATH_WINDOW_PROPERTY);
        
        
        // Workspace role nodes
        Map<WorkspaceRole, SynthesisNode> roleNodes = new HashMap<>(WorkspaceRole.values().length);
        for (WorkspaceRole workspaceRole : WorkspaceRole.values()) {
            SynthesisNode node = this.applicationContext.getBean(SynthesisNode.class, workspaceRole.getId(), SynthesisNodeType.ROLE);
            node.setDisplayName(bundle.getString("SYNTHESIS_ROOT_" + workspaceRole.getKey()));

            roleNodes.put(workspaceRole, node);
        }


        if (path != null) {
            // Nuxeo document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
            // Nuxeo document
            Document document = documentContext.getDocument();

            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetPermissionsCommand.class, document);
            JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(command);

            if (result != null) {
                // JSON local array
                JSONArray localArray = result.getJSONArray("local");
                // JSON inherited array
                JSONArray inheritedArray = result.getJSONArray("inherited");
                // Merged JSON arrays
                JSONArray[] arrays = new JSONArray[]{localArray, inheritedArray};

                
                // Node workspace roles
                Map<SynthesisNode, WorkspaceRole> nodeRoles = new HashMap<>(localArray.size() + inheritedArray.size());

                for (JSONArray array : arrays) {
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
                                permission = this.applicationContext.getBean(Permission.class);
                                permission.setName(name);
                                permission.setValues(new ArrayList<String>());
                                permission.setGroup(group);
                                permissions.put(name, permission);
                            }
                            permission.getValues().add(object.getString("permission"));
                        }
                    }

                    for (Entry<String, Permission> entry : permissions.entrySet()) {
                        // Name
                        String name = entry.getKey();
                        // Permission
                        Permission permission = entry.getValue();
                        // Permission values
                        String[] values = permission.getValues().toArray(new String[permission.getValues().size()]);
                        // Workspace role
                        WorkspaceRole role = WorkspaceRole.fromPermissions(values);

                        if (role == null) {
                            continue;
                        } else {
                            // Node type
                            SynthesisNodeType type;
                            if (Permission.PUBLIC_NAME.equals(name)) {
                                type = SynthesisNodeType.PUBLIC_GROUP;
                            } else if (permission.isGroup()) {
                                type = SynthesisNodeType.GROUP;
                            } else {
                                type = SynthesisNodeType.USER;
                            }

                            // Node
                            SynthesisNode node = this.applicationContext.getBean(SynthesisNode.class, name, type);

                            if (nodeRoles.containsKey(node)) {
                                // Existing workspace role
                                WorkspaceRole existingRole = nodeRoles.get(node);

                                if (role.getWeight() > existingRole.getWeight()) {
                                    nodeRoles.put(node, role);
                                }
                            } else {
                                nodeRoles.put(node, role);
                            }
                        }
                    }
                }


                // Workspace members
                List<WorkspaceMember> memberList = this.workspaceService.getAllMembers(workspaceId);
                Map<String, WorkspaceMember> memberMap = new HashMap<>(memberList.size());
                for (WorkspaceMember member : memberList) {
                    Person person = member.getMember();
                    memberMap.put(person.getUid(), member);
                }

                // Workspace groups
                List<CollabProfile> groupList = this.workspaceService.findByWorkspaceId(workspaceId);
                Map<String, CollabProfile> groupMap = new HashMap<>(groupList.size());
                Map<String, List<WorkspaceMember>> groupMemberMap = new HashMap<>(groupList.size());
                for (CollabProfile group : groupList) {
                    groupMap.put(group.getCn(), group);

                    List<Name> names = group.getUniqueMember();
                    List<WorkspaceMember> members = new ArrayList<>(names.size());
                    for (Name name : names) {
                        if (!name.isEmpty()) {
                            String uid = StringUtils.substringAfter(name.get(name.size() - 1), "=");
                            WorkspaceMember member = memberMap.get(uid);
                            if (member != null) {
                                members.add(member);
                            }
                        }
                    }
                    groupMemberMap.put(group.getCn(), members);
                }


                for (Entry<SynthesisNode, WorkspaceRole> entry : nodeRoles.entrySet()) {
                    // Node
                    SynthesisNode node = entry.getKey();
                    // Workspace role
                    WorkspaceRole role = entry.getValue();

                    // Node display name
                    String displayName = null;
                    // Group
                    CollabProfile group = null;

                    if (SynthesisNodeType.PUBLIC_GROUP.equals(node.getType())) {
                        displayName = bundle.getString("SYNTHESIS_PUBLIC_GROUP");
                    } else if (SynthesisNodeType.GROUP.equals(node.getType())) {
                        group = groupMap.get(node.getName());

                        if (group != null) {
                            if (WorkspaceGroupType.space_group.equals(group.getType())) {
                                displayName = bundle.getString("ALL_WORKSPACE_MEMBERS");
                            } else if (WorkspaceGroupType.security_group.equals(group.getType())) {
                                displayName = bundle.getString("SYNTHESIS_" + group.getRole().getKey());
                            } else {
                                displayName = StringUtils.defaultIfBlank(group.getDisplayName(), group.getCn());
                            }
                        }
                    } else {
                        WorkspaceMember member = memberMap.get(node.getName());
                        if (member != null) {
                            Person person = member.getMember();
                            displayName = StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid());
                        }
                    }

                    if (StringUtils.isBlank(displayName)) {
                        // Hide blank nodes
                        continue;
                    } else {
                        node.setDisplayName(displayName);
                    }


                    // Role node
                    SynthesisNode roleNode = roleNodes.get(role);
                    roleNode.getChildren().add(node);


                    // Group members
                    if (group != null) {
                        List<WorkspaceMember> members = groupMemberMap.get(group.getCn());

                        for (WorkspaceMember member : members) {
                            // Person
                            Person person = member.getMember();

                            // Member node
                            SynthesisNode memberNode = this.applicationContext.getBean(SynthesisNode.class, person.getUid(), SynthesisNodeType.USER);
                            memberNode.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid()));

                            node.getChildren().add(memberNode);
                        }
                    }
                }
            }
        }


        // Synthesis nodes
        SortedSet<SynthesisNode> nodes = new TreeSet<>();
        nodes.addAll(roleNodes.values());

        return nodes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
