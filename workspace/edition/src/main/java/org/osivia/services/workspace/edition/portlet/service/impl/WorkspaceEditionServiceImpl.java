package org.osivia.services.workspace.edition.portlet.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.comparator.TasksComparator;
import org.osivia.services.workspace.edition.portlet.repository.WorkspaceEditionRepository;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.osivia.services.workspace.task.creation.portlet.repository.WorkspaceTaskCreationRepository;
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
        WorkspaceEditionForm form = this.repository.getForm(portalControllerContext);

        // Sort tasks
        Collections.sort(form.getTasks(), this.tasksComparator);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Tasks
        List<Task> tasks = form.getTasks();

        // Sort tasks
        Collections.sort(tasks, this.tasksComparator);
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


        // Internationalization fragment
        String fragment;
        if ("Room".equals(form.getType())) {
            fragment = bundle.getString("WORKSPACE_EDITION_ROOM_FRAGMENT");
        } else {
            fragment = bundle.getString("WORKSPACE_EDITION_WORKSPACE_FRAGMENT");
        }


        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_EDITION_SUCCESS", fragment);
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createTask(PortalControllerContext portalControllerContext, WorkspaceEditionForm form)
            throws PortletException {
        // Task creation form
        TaskCreationForm taskCreationForm = form.getTaskCreationForm();
        
        if ((taskCreationForm != null) && taskCreationForm.isValid()) {
            // Tasks
            List<Task> tasks = form.getTasks();

            // New task
            Task task = this.repository.createTask(portalControllerContext, taskCreationForm);
            task.setActive(true);
            task.setOrder(tasks.size() + 1);

            // Update model
            tasks.add(task);
            form.setTaskCreationForm(null);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Delete
        this.repository.delete(portalControllerContext, form);

        // Redirection URL
        String url;
        try {
            url = this.portalUrlFactory.getDestroyCurrentPageUrl(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, form.getPath(), null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null,
                null, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskCreationUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(WorkspaceTaskCreationRepository.WORKSPACE_TYPE_WINDOW_PROPERTY, form.getType());
        
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-task-creation-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        return url;
    }

}
