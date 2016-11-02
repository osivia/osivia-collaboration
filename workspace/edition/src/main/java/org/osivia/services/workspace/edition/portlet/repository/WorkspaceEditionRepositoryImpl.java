package org.osivia.services.workspace.edition.portlet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.WorkspaceService;
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
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.Vignette;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Workspace edition repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceEditionRepository
 * @see ApplicationContextAware
 */
@Repository
public class WorkspaceEditionRepositoryImpl implements WorkspaceEditionRepository, ApplicationContextAware {

    /** Current workspace attribute name. */
    private static final String CURRENT_WORKSPACE_ATTRIBUTE = "osivia.workspace.edition.currentWorkspace";


    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Application context. */
    private ApplicationContext applicationContext;


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
    public Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        
        // Workspace Nuxeo document
        Document workspace = (Document) request.getAttribute(CURRENT_WORKSPACE_ATTRIBUTE);
        
        if (workspace == null) {
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
            workspace = documentContext.getDoc();

            request.setAttribute(CURRENT_WORKSPACE_ATTRIBUTE, workspace);
        }

        return workspace;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Vignette getVignette(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        // Vignette properties
        PropertyMap properties = workspace.getProperties().getMap("ttc:vignette");
        
        // Vignette URL
        String url;
        if ((properties == null) || StringUtils.isEmpty(properties.getString("data"))) {
            url = null;
        } else {
            url = nuxeoController.createFileLink(workspace, "ttc:vignette");
        }

        // Vignette
        Vignette vignette = this.applicationContext.getBean(Vignette.class);
        vignette.setUrl(url);

        return vignette;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Taskbar tasks & taskbar items
        List<TaskbarTask> taskbarTasks;
        TaskbarItems taskbarItems;
        try {
            taskbarTasks = this.taskbarService.getTasks(portalControllerContext, path, false);
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
            Task task = this.applicationContext.getBean(Task.class, taskbarTask);
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
            Task task = this.applicationContext.getBean(Task.class, item);

            // Display name
            String displayName = bundle.getString(item.getKey(), item.getCustomizedClassLoader());
            task.setDisplayName(displayName);

            tasks.add(task);
        }

        return tasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options, WorkspaceEditionForm form) throws PortletException {
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
        INuxeoCommand command = this.applicationContext.getBean(WorkspaceEditionCommand.class, options, form, items, bundle);
        nuxeoController.executeNuxeoCommand(command);


        // Reload vignette
        try {
            nuxeoController.fetchFileContent(options.getPath(), "ttc:vignette", true);
        } catch (NuxeoException e) {
            // Do nothing: maybe the vignette does not exist
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document types
        Map<String, DocumentType> documentTypes = nuxeoController.getCMSItemTypes();

        // Type
        String createdType = form.getType();
        DocumentType documentType = documentTypes.get(createdType);

        // Taskbar factory
        TaskbarFactory factory = this.taskbarService.getFactory();
        // Taskbar item
        TaskbarItem item = factory.createCmsTaskbarItem(null, null, documentType.getGlyph(), createdType);

        // New task
        Task task = this.applicationContext.getBean(Task.class, item);
        task.setDisplayName(form.getTitle());
        task.setDescription(form.getDescription());

        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Workspace identifier
        Document workspace = options.getWorkspace();
        String workspaceId = workspace.getString("webc:url");

        this.workspaceService.delete(workspaceId);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteWorkspaceCommand.class, options.getPath());
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
