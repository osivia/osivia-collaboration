package org.osivia.services.workspace.edition.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.workspace.edition.portlet.model.Editorial;
import org.osivia.services.workspace.edition.portlet.model.Image;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceEditionRepository
 * @see ApplicationContextAware
 */
@Repository
public class WorkspaceEditionRepositoryImpl implements WorkspaceEditionRepository, ApplicationContextAware, PortletContextAware {

    /** Current workspace attribute name. */
    private static final String CURRENT_WORKSPACE_ATTRIBUTE = "osivia.workspace.edition.currentWorkspace";


    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDao;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Application context. */
    private ApplicationContext applicationContext;
    /** Portlet context. */
    private PortletContext portletContext;


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
    public Image getVignette(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
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
        Image vignette = this.applicationContext.getBean(Image.class);
        vignette.setUrl(url);

        return vignette;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Image getBanner(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Banner properties
        PropertyMap properties = workspace.getProperties().getMap("ttcs:headImage");

        // Banner URL
        String url;
        if ((properties == null) || StringUtils.isEmpty(properties.getString("data"))) {
            url = null;
        } else {
            url = nuxeoController.createFileLink(workspace, "ttcs:headImage");
        }

        // Banner
        Image banner = this.applicationContext.getBean(Image.class);
        banner.setUrl(url);

        return banner;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Taskbar tasks & taskbar items
        List<TaskbarTask> taskbarTasks;
        TaskbarItems taskbarItems;
        try {
            taskbarTasks = this.taskbarService.getTasks(portalControllerContext, workspace.getPath(), false);
            taskbarItems = this.taskbarService.getItems(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }


        // Available taskbar items
        List<TaskbarItem> availableItems = new ArrayList<>(taskbarItems.getAll().size());
        for (TaskbarItem item : taskbarItems.getAll()) {
            if (!TaskbarItemType.TRANSVERSAL.equals(item.getType()) && !item.isHidden()) {
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
            if (!taskbarTask.isHidden()) {
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

                // Custom task indicator
                task.setCustom(taskbarTask.getKey() == null);

                tasks.add(task);
            }
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
    public Editorial getEditorial(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Task
        TaskbarTask task;
        try {
            task = this.taskbarService.getTask(portalControllerContext, workspace.getPath(), WorkspaceEditionService.WORKSPACE_EDITORIAL_TASK_ID);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Editorial
        Editorial editorial = this.applicationContext.getBean(Editorial.class);

        if (task != null) {
            // Nuxeo document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(task.getPath());
            // Nuxeo document
            Document document = documentContext.getDoc();

            // Displayed indicator
            boolean displayed = !task.isDisabled();
            editorial.setDisplayed(displayed);
            editorial.setInitialDisplayed(displayed);

            // Document DTO
            DocumentDTO documentDto = this.documentDao.toDTO(document);
            editorial.setDocument(documentDto);
        }

        return editorial;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Editorial createEditorial(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        Task task = getEditorialTask(portalControllerContext, null);

        if (task != null) {
            task.setActive(true);

            List<Task> tasks = Arrays.asList(new Task[]{task});
            this.updateTasks(portalControllerContext, workspace, tasks);
        }

        return this.getEditorial(portalControllerContext, workspace);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPermissions(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(workspace.getPath());
        // Permissions
        BasicPermissions permissions = documentContext.getPermissions(BasicPermissions.class);

        return permissions.isManageableByUser();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWorkspaceType(PortalControllerContext portalControllerContext, Document workspace, WorkspaceType workspaceType) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateWorkspaceTypeCommand.class, workspace, workspaceType);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTasks(PortalControllerContext portalControllerContext, Document workspace, List<Task> tasks) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateTasksCommand.class, workspace, tasks);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateProperties(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdatePropertiesCommand.class, form);
        nuxeoController.executeNuxeoCommand(command);


        // Reload vignette
        try {
            nuxeoController.fetchFileContent(form.getDocument().getPath(), "ttc:vignette", true);
        } catch (NuxeoException e) {
            // Do nothing: maybe the vignette does not exist
        }

        // Reload banner
        try {
            nuxeoController.fetchFileContent(form.getDocument().getPath(), "ttcs:headImage", true);
        } catch (NuxeoException e) {
            // Do nothing: maybe the banner does not exist
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEditorial(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Editorial
        Editorial editorial = form.getEditorial();
        // Editorial document DTO
        DocumentDTO document = editorial.getDocument();
        
        if (document != null) {
            if (BooleanUtils.xor(new boolean[]{editorial.isDisplayed(), editorial.isInitialDisplayed()})) {
                // Task
                Task task = this.getEditorialTask(portalControllerContext, document);
                if (task == null) {
                    throw new PortletException("Workspace editorial task not found.");
                } else {
                    task.setActive(editorial.isDisplayed());
                    task.setUpdated(true);

                    List<Task> tasks = Arrays.asList(new Task[]{task});
                    this.updateTasks(portalControllerContext, form.getDocument(), tasks);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkTitleAvailability(WorkspaceEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CheckTitleAvailabilityCommand.class, form);
        Boolean available = (Boolean) nuxeoController.executeNuxeoCommand(command);

        return BooleanUtils.isTrue(available);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkWebIdAvailability(String webId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CheckWebIdAvailabilityCommand.class, webId);
        Boolean available = (Boolean) nuxeoController.executeNuxeoCommand(command);

        return BooleanUtils.isTrue(available);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(true);

        // Workspace identifier
        Document workspace = form.getDocument();
        String workspaceId = workspace.getString("webc:url");

        this.workspaceService.delete(workspaceId);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteWorkspaceCommand.class, workspace.getPath());
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * Get editorial task.
     * 
     * @param portalControllerContext portal controller context
     * @param document editorial document DTO, may be null
     * @return task
     * @throws PortletException
     */
    private Task getEditorialTask(PortalControllerContext portalControllerContext, DocumentDTO document) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Item
        TaskbarItem item;
        try {
            item = this.taskbarService.getItem(portalControllerContext, WorkspaceEditionService.WORKSPACE_EDITORIAL_TASK_ID);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Task
        Task task;
        if (item == null) {
            task = null;
        } else {
            task = new Task(item);

            if (document != null) {
                task.setPath(document.getPath());
            }
            
            // Display name
            String displayName = bundle.getString(item.getKey(), item.getCustomizedClassLoader());
            task.setDisplayName(displayName);
        }
        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
