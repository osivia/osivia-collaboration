package org.osivia.services.workspace.portlet.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.portlet.model.CreateTaskForm;
import org.osivia.services.workspace.portlet.model.Task;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.portlet.model.comparator.AlphaOrderComparator;
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

    /** Alpha order comparator. */
    @Autowired
    private AlphaOrderComparator alphaOrderComparator;

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
    public CreateTaskForm getCreateTaskForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Task document types
        List<DocumentType> documentTypes = this.repository.getTaskTypes(portalControllerContext);

        // Types
        SortedMap<String, DocumentType> types = new TreeMap<>(this.alphaOrderComparator);
        for (DocumentType type : documentTypes) {
            String displayName = bundle.getString(StringUtils.upperCase(type.getName()), type.getCustomizedClassLoader());

            types.put(displayName, type);
        }
        
        // Form
        CreateTaskForm form = new CreateTaskForm();
        form.setTypes(types);
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
    public void createTask(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, CreateTaskForm createTaskForm) throws PortletException {
        // Tasks
        List<Task> tasks = form.getTasks();

        // New task
        Task task = this.repository.createTask(portalControllerContext, createTaskForm);
        task.setActive(true);
        task.setOrder(tasks.size() + 1);

        // Update tasks
        tasks.add(task);
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

}
