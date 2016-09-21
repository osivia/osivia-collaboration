package org.osivia.services.workspace.service;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.model.WorkspaceCreationForm;
import org.osivia.services.workspace.repository.WorkspaceCreationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Workspace creation service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationService
 */
@Service
public class WorkspaceCreationServiceImpl implements WorkspaceCreationService {

    /** Workspace creation repository. */
    @Autowired
    private WorkspaceCreationRepository repository;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public WorkspaceCreationServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Create workspace Nuxeo document
        Document workspace = this.repository.createDocument(portalControllerContext, form);
        // Create LDAP groups
        this.repository.createGroups(portalControllerContext, workspace);
        // Update permissions
        this.repository.updatePermissions(portalControllerContext, workspace);

        // Update form
        form.setTitle(null);
        form.setDescription(null);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_CREATION_SUCCESS", workspace.getTitle());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }

}
