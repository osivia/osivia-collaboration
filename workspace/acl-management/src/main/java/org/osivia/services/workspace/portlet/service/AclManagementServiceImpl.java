package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AclEntry;
import org.osivia.services.workspace.portlet.model.AddForm;
import org.osivia.services.workspace.portlet.model.Record;
import org.osivia.services.workspace.portlet.model.RecordType;
import org.osivia.services.workspace.portlet.model.Role;
import org.osivia.services.workspace.portlet.model.SynthesisNode;
import org.osivia.services.workspace.portlet.model.SynthesisNodeType;
import org.osivia.services.workspace.portlet.repository.AclManagementRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Workspace ACL management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AclManagementService
 * @see ApplicationContextAware
 */
@Service
public class AclManagementServiceImpl implements AclManagementService, ApplicationContextAware {

    /** Workspace ACL management repository. */
    @Autowired
    private AclManagementRepository repository;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public AclManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AclEntries getAclEntries(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

        // ACL entries
        return this.repository.getAclEntries(portalControllerContext, workspaceId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AddForm getAddForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

        // Add form
        AddForm form = this.applicationContext.getBean(AddForm.class);

        // Records
        List<Record> groupRecords = this.repository.getGroupRecords(portalControllerContext, workspaceId);
        List<Record> userRecords = this.repository.getUserRecords(portalControllerContext, workspaceId);
        List<Record> records = new ArrayList<>(groupRecords.size() + userRecords.size());
        records.addAll(groupRecords);
        records.addAll(userRecords);
        form.setRecords(records);

        // Default role
        Role defaultRole = this.repository.getDefaultRole(portalControllerContext);
        form.setRole(defaultRole);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Role> getRoles(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getRoles(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void displayControls(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        entries.setDisplayControls(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void changeInheritance(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
        // Current user
        String user = request.getRemoteUser();

        // User ACL entry
        AclEntry userEntry = null;
        for (AclEntry entry : entries.getEntries()) {
            if (StringUtils.equals(user, entry.getId())) {
                userEntry = entry;
                break;
            }
        }

        if (entries.isInherited()) {
            // Remove previous user ACL entry
            if (userEntry != null) {
                entries.getEntries().remove(userEntry);
            }

            // Restore saved user ACL entry
            if (entries.getSavedUserEntry() != null) {
                entries.getEntries().add(entries.getSavedUserEntry());
            }
        } else {
            // Save user ACL entry
            if (userEntry != null) {
                entries.setSavedUserEntry(userEntry);
                entries.getEntries().remove(userEntry);
            }

            // Workspace member
            WorkspaceMember member = this.workspaceService.getMember(entries.getWorkspaceId(), user);

            if (member != null) {
                // Person
                Person person = member.getMember();

                // Role
                WorkspaceRole workspaceRole = WorkspaceRole.ADMIN;
                Role role = this.applicationContext.getBean(Role.class, workspaceRole);
                role.setDisplayName(bundle.getString(workspaceRole.getKey(), workspaceRole.getClassLoader()));

                // Add user administrator ACL entry
                AclEntry entry = this.applicationContext.getBean(AclEntry.class);
                entry.setId(user);
                entry.setType(RecordType.USER);
                entry.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid()));
                entry.setAvatar(person.getAvatar().getUrl());
                entry.setExtra(person.getMail());
                entry.setRole(role);
                entry.setEditable(false);

                entries.getEntries().add(entry);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(PortalControllerContext portalControllerContext, AclEntries entries, List<Role> roles) throws PortletException {
        this.repository.update(portalControllerContext, entries, roles);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelUpdate(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        this.repository.cancelUpdate(portalControllerContext, entries);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void add(PortalControllerContext portalControllerContext, AclEntries entries, List<Role> roles, AddForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Selected records
        List<Record> selectedRecords = new ArrayList<>(form.getIdentifiers().size());
        for (Record record : form.getRecords()) {
            if (form.getIdentifiers().contains(record.getId())) {
                selectedRecords.add(record);
            }
        }

        this.repository.add(portalControllerContext, entries, selectedRecords, form.getRole());

        // Notification
        String message = bundle.getString("MESSAGE_SUCCESS_ADD_ACL");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        this.repository.reset(portalControllerContext, entries);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getRedirectionUrl(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        // Path
        String path = entries.getDocument().getPath();
        // CMS URL
        String cmsUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, null, null, null, null, null);

        return this.portalUrlFactory.adaptPortalUrlToPopup(portalControllerContext, cmsUrl, IPortalUrlFactory.POPUP_URL_ADAPTER_CLOSE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getSynthesisData(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

        // Synthesis nodes
        SortedSet<SynthesisNode> nodes = this.repository.getSynthesisNodes(portalControllerContext, workspaceId);

        // JSON array
        JSONArray array = this.toJSONArray(nodes);

        return array.toString();
    }


    /**
     * Transform synthesis nodes to JSON array.
     * 
     * @param nodes synthesis nodes
     * @return JSON array
     */
    private final JSONArray toJSONArray(SortedSet<SynthesisNode> nodes) {
        // JSON array
        JSONArray array = new JSONArray();

        for (SynthesisNode node : nodes) {
            if (!SynthesisNodeType.ROLE.equals(node.getType()) || CollectionUtils.isNotEmpty(node.getChildren())) {
                JSONObject object = this.toJSONObject(node);

                if (CollectionUtils.isNotEmpty(node.getChildren())) {
                    JSONArray children = this.toJSONArray(node.getChildren());
                    object.put("children", children);
                }

                array.add(object);
            }
        }

        return array;
    }


    /**
     * Transform synthesis node to JSON object.
     * 
     * @param node synthesis node
     * @return JSON object
     */
    private final JSONObject toJSONObject(SynthesisNode node) {
        JSONObject object = new JSONObject();
        object.put("title", node.getDisplayName());
        object.put("folder", SynthesisNodeType.ROLE.equals(node.getType()) || SynthesisNodeType.GROUP.equals(node.getType()));
        object.put("lazy", false);
        object.put("expanded", SynthesisNodeType.ROLE.equals(node.getType()));
        object.put("iconclass", node.getType().getIcon());
        
        return object;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
