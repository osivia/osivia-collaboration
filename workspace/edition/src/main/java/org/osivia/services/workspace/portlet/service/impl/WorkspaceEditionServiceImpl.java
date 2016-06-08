package org.osivia.services.workspace.portlet.service.impl;

import java.util.Collections;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.portlet.model.comparator.TasksComparator;
import org.osivia.services.workspace.portlet.repository.WorkspaceEditionRepository;
import org.osivia.services.workspace.portlet.service.WorkspaceEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Workspace edition service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceEditionService
 */
@Service
public class WorkspaceEditionServiceImpl implements WorkspaceEditionService {

    /** Workspace edition repository. */
    @Autowired
    private WorkspaceEditionRepository repository;

    /** Tasks comparator. */
    @Autowired
    private TasksComparator tasksComparator;

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
    public WorkspaceEditionServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getForm(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Sort tasks
        Collections.sort(form.getTasks(), this.tasksComparator);

        // Save
        this.repository.save(portalControllerContext, form);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_EDITION_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, form.getPath(), null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null,
                null, null);
    }

}
