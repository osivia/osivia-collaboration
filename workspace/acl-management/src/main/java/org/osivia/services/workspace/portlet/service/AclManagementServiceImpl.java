package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AddForm;
import org.osivia.services.workspace.portlet.model.Record;
import org.osivia.services.workspace.portlet.model.Role;
import org.osivia.services.workspace.portlet.repository.AclManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Workspace ACL management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AclManagementService
 */
@Service
public class AclManagementServiceImpl implements AclManagementService {

    /** Workspace ACL management repository. */
    @Autowired
    private AclManagementRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


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
        AddForm form = new AddForm();

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
    public void changeInheritance(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException {
        this.repository.changeInheritance(portalControllerContext, entries);
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

}
