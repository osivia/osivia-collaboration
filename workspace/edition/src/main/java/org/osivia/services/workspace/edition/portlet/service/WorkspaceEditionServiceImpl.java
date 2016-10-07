package org.osivia.services.workspace.edition.portlet.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
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
import org.osivia.services.workspace.edition.portlet.model.Vignette;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;
import org.osivia.services.workspace.edition.portlet.model.comparator.TasksComparator;
import org.osivia.services.workspace.edition.portlet.repository.WorkspaceEditionRepository;
import org.osivia.services.workspace.task.creation.portlet.repository.WorkspaceTaskCreationRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceEditionService
 * @see ApplicationContextAware
 */
@Service
public class WorkspaceEditionServiceImpl implements WorkspaceEditionService, ApplicationContextAware {

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


    /** Application context. */
    private ApplicationContext applicationContext;


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
    public WorkspaceEditionOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Options
        WorkspaceEditionOptions options = this.applicationContext.getBean(WorkspaceEditionOptions.class);

        // Workspace document
        Document workspace = this.repository.getWorkspace(portalControllerContext);

        options.setWorkspace(workspace);
        options.setPath(workspace.getPath());
        options.setType(workspace.getType());

        return options;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        WorkspaceEditionForm form = this.applicationContext.getBean(WorkspaceEditionForm.class);

        // Workspace document
        Document workspace = this.repository.getWorkspace(portalControllerContext);

        form.setTitle(workspace.getTitle());
        form.setDescription(workspace.getString("dc:description"));

        // Workspace root type indicator
        boolean root = "Workspace".equals(workspace.getType());
        form.setRoot(root);

        // Type
        if (root) {
            String visibility = workspace.getString("ttcs:visibility");
            if (StringUtils.isNotEmpty(visibility)) {
                form.setType(WorkspaceType.valueOf(visibility));
            }
        }

        // Vignette
        Vignette vignette = this.repository.getVignette(portalControllerContext, workspace);
        form.setVignette(vignette);

        // Tasks
        List<Task> tasks = this.repository.getTasks(portalControllerContext, workspace.getPath());
        Collections.sort(tasks, this.tasksComparator);
        form.setTasks(tasks);

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
    public void uploadVignette(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException, IOException {
        // Vignette
        Vignette vignette = form.getVignette();
        vignette.setUpdated(true);
        vignette.setDeleted(false);

        // Temporary file
        MultipartFile upload = form.getVignette().getUpload();
        File temporaryFile = File.createTempFile("vignette-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        vignette.setTemporaryFile(temporaryFile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVignette(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Vignette
        Vignette vignette = form.getVignette();
        vignette.setUpdated(false);
        vignette.setDeleted(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options, WorkspaceEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Sort tasks
        Collections.sort(form.getTasks(), this.tasksComparator);

        // Save
        this.repository.save(portalControllerContext, options, form);


        // Internationalization fragment
        String fragment;
        if ("Room".equals(options.getType())) {
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
    public void createTask(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
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
    public String delete(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException {
        // Delete
        this.repository.delete(portalControllerContext, options);

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
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException {
        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, options.getPath(), null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null,
                null, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskCreationUrl(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(WorkspaceTaskCreationRepository.WORKSPACE_TYPE_WINDOW_PROPERTY, options.getType());

        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-task-creation-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
