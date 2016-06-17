package org.osivia.services.workspace.portlet.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.workspace.portlet.model.CreateTaskForm;
import org.osivia.services.workspace.portlet.model.Task;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.portlet.repository.WorkspaceEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Workspace edition repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceEditionRepository
 */
@Repository
public class WorkspaceEditionRepositoryImpl implements WorkspaceEditionRepository {

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public WorkspaceEditionRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // CMS base path
        String basePath = nuxeoController.getBasePath();
        if (basePath == null) {
            throw new PortletException(bundle.getString("MESSAGE_WORKSPACE_PATH_UNDEFINED"));
        }

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath, true);
        // Nuxeo document
        Document workspace = documentContext.getDoc();


        // Taskbar tasks & taskbar items
        List<TaskbarTask> taskbarTasks;
        TaskbarItems taskbarItems;
        try {
            taskbarTasks = this.taskbarService.getTasks(portalControllerContext, basePath, false);
            taskbarItems = this.taskbarService.getItems(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }


        // Available taskbar items
        List<TaskbarItem> availableItems = new ArrayList<>(taskbarItems.getAll().size());
        for (TaskbarItem item : taskbarItems.getAll()) {
            if (!TaskbarItemType.TRANSVERSAL.equals(item.getType())) {
                availableItems.add(item);
            }
        }
        for (TaskbarTask taskbarTask : taskbarTasks) {
            TaskbarItem item = taskbarItems.get(taskbarTask.getId());
            if (item != null) {
                availableItems.remove(item);
            }
        }


        // Tasks
        List<Task> tasks = new ArrayList<Task>(taskbarTasks.size() + availableItems.size());
        int order = 1;
        for (TaskbarTask taskbarTask : taskbarTasks) {
            Task task = new Task(taskbarTask);
            task.setPath(taskbarTask.getPath());

            // Display name
            String displayName;
            if (taskbarTask.getKey() != null) {
                displayName = bundle.getString(taskbarTask.getKey(), taskbarTask.getCustomizedClassLoader());
            } else {
                displayName = taskbarTask.getTitle();
            }
            task.setDisplayName(displayName);

            // Order
            if (!taskbarTask.isDisabled()) {
                task.setActive(!taskbarTask.isDisabled());
                task.setOrder(order++);
            }

            tasks.add(task);
        }
        for (TaskbarItem item : availableItems) {
            Task task = new Task(item);

            // Display name
            String displayName = bundle.getString(item.getKey(), item.getCustomizedClassLoader());
            task.setDisplayName(displayName);

            tasks.add(task);
        }


        // Form
        WorkspaceEditionForm form = new WorkspaceEditionForm();
        form.setPath(workspace.getPath());
        form.setTitle(workspace.getTitle());
        form.setDescription(workspace.getString("dc:description"));
        form.setTasks(tasks);
        form.setType(workspace.getType());

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document types
        Map<String, DocumentType> documentTypes = nuxeoController.getCMSItemTypes();

        // Workspace document type
        DocumentType workspaceType = documentTypes.get("Workspace");

        // Workspace sub-types
        List<String> workspaceSubTypes;
        if (workspaceType == null) {
            workspaceSubTypes = new ArrayList<>(0);
        } else {
            workspaceSubTypes = workspaceType.getPortalFormSubTypes();
        }

        // Types
        List<DocumentType> types = new ArrayList<>(workspaceSubTypes.size());
        for (String subType : workspaceSubTypes) {
            DocumentType type = documentTypes.get(subType);
            if (type != null) {
                types.add(type);
            }
        }

        return types;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Default taskbar items
        SortedSet<TaskbarItem> items;
        try {
            items = this.taskbarService.getDefaultItems(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Nuxeo command
        INuxeoCommand command = new WorkspaceEditionCommand(form, items, bundle);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(PortalControllerContext portalControllerContext, CreateTaskForm createTaskForm) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document types
        Map<String, DocumentType> documentTypes = nuxeoController.getCMSItemTypes();
        
        // Type
        String createdType = createTaskForm.getCreatedType();
        DocumentType documentType = documentTypes.get(createdType);
        
        // Taskbar factory
        TaskbarFactory factory = taskbarService.getFactory();
        // Taskbar item
        TaskbarItem item = factory.createCmsTaskbarItem(null, null, documentType.getGlyph(), createdType);

        // New task
        Task task = new Task(item);
        task.setDisplayName(createTaskForm.getCreatedTitle());
        task.setDescription(createTaskForm.getCreatedDescription());

        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
     // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = new DeleteWorkspaceCommand(form.getPath());
        nuxeoController.executeNuxeoCommand(command);
    }

}
