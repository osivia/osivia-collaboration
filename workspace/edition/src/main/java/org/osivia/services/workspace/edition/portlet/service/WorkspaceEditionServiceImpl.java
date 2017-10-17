package org.osivia.services.workspace.edition.portlet.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.CMSObjectPath;
import org.osivia.services.workspace.edition.portlet.model.Editorial;
import org.osivia.services.workspace.edition.portlet.model.Image;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.comparator.TasksComparator;
import org.osivia.services.workspace.edition.portlet.repository.WorkspaceEditionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Role service. */
    @Autowired
    private RoleService roleService;


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
    public WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace document
        Document workspace = this.repository.getWorkspace(portalControllerContext);
        
        // User
        String user = portalControllerContext.getRequest().getRemoteUser();
        // User DN
        Name dn = this.personService.getEmptyPerson().buildDn(user);
        
        // Administrator indicator
        boolean admin = this.roleService.hasRole(dn, "role_workspace-management");

        // Form
        WorkspaceEditionForm form = this.applicationContext.getBean(WorkspaceEditionForm.class, workspace, admin);
        form.setTitle(workspace.getTitle());
        form.setDescription(workspace.getString("dc:description"));

        // Workspace root type indicator
        boolean root = "Workspace".equals(workspace.getType());
        form.setRoot(root);

        // Templates
        if (root) {
            form.setTemplate(StringUtils.trimToEmpty(workspace.getString("ttc:pageTemplate")));

            Map<String, String> templates = this.repository.getTemplates(portalControllerContext, workspace);
            form.setTemplates(templates);
        }

        // Workspace type
        if (root) {
            String visibility = workspace.getString("ttcs:visibility");
            if (StringUtils.isNotEmpty(visibility)) {
                WorkspaceType workspaceType = WorkspaceType.valueOf(visibility);
                form.setWorkspaceType(workspaceType);
                form.setInitialWorkspaceType(workspaceType);
            }
        }

        // Visual
        Image visual = this.repository.getVisual(portalControllerContext, workspace);
        form.setVisual(visual);
        
        // Tasks
        List<Task> tasks = this.repository.getTasks(portalControllerContext, workspace);
        Collections.sort(tasks, this.tasksComparator);
        form.setTasks(tasks);

        // Editorial
        Editorial editorial = this.repository.getEditorial(portalControllerContext, workspace);
        form.setEditorial(editorial);

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
    public void uploadVisual(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException, IOException {
        // Visual
        Image visual = form.getVisual();
        visual.setUpdated(true);
        visual.setDeleted(false);

        // Temporary file
        MultipartFile upload = visual.getUpload();
        File temporaryFile = File.createTempFile("visual-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        visual.setTemporaryFile(temporaryFile);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVisual(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Visual
        Image visual = form.getVisual();
        visual.setUpdated(false);
        visual.setDeleted(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createEditorial(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, BindingResult result) throws PortletException {
        // Editorial webId
        StringBuilder webId = new StringBuilder();
        webId.append(ITaskbarService.WEBID_PREFIX);
        webId.append(form.getDocument().getString("webc:url"));
        webId.append("_");
        webId.append(StringUtils.lowerCase(WORKSPACE_EDITORIAL_TASK_ID));

        if (this.repository.checkWebIdAvailability(webId.toString())) {
            Editorial editorial = this.repository.createEditorial(portalControllerContext, form.getDocument());
            form.setEditorial(editorial);
        } else {
            result.rejectValue("editorial.displayed", "NotAvailable", null);
            form.getEditorial().setDisplayed(false);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace or room Nuxeo document
        Document workspace = form.getDocument();

        // Internationalization fragment
        String fragment;
        if ("Room".equals(form.getDocument().getType())) {
            fragment = bundle.getString("WORKSPACE_EDITION_ROOM_FRAGMENT");
        } else {
            fragment = bundle.getString("WORKSPACE_EDITION_WORKSPACE_FRAGMENT");
        }


        if (this.repository.checkPermissions(portalControllerContext, workspace)) {
            // Update workspace type
            if (form.isRoot()) {
                WorkspaceType workspaceType = form.getWorkspaceType();
                WorkspaceType initialWorkspaceType = form.getInitialWorkspaceType();

                if (!workspaceType.equals(initialWorkspaceType) && (form.isAdmin() || !initialWorkspaceType.isPortalAdministratorRestriction())) {
                    this.repository.updateWorkspaceType(portalControllerContext, workspace, workspaceType);
                }
            }


            // Update tasks
            List<Task> tasks = form.getTasks();
            Collections.sort(tasks, this.tasksComparator);
            this.repository.updateTasks(portalControllerContext, workspace, tasks);

            // Update properties
            this.repository.updateProperties(portalControllerContext, form);

            // Update editorial
            this.repository.updateEditorial(portalControllerContext, form);
            

            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_EDITION_SUCCESS", fragment);
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } else {
            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_EDITION_FORBIDDEN_ERROR", fragment);
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Errors errors, WorkspaceEditionForm form) {
        // Available workspace title indicator
        try {
            if (!this.repository.checkTitleAvailability(form)) {
                errors.rejectValue("title", "NotAvailable");
            }
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }
        
        
        for (Task task : form.getTasks()) {
            if (task.isActive() && StringUtils.isEmpty(task.getPath())) {
                // Check webId availability
                StringBuilder webId = new StringBuilder();
                webId.append(ITaskbarService.WEBID_PREFIX);
                webId.append(form.getDocument().getString("webc:url"));
                webId.append("_");
                webId.append(StringUtils.lowerCase(task.getId()));

                try {
                    if (!this.repository.checkWebIdAvailability(webId.toString())) {
                        Object arguments[] = new Object[]{task.getDisplayName()};
                        errors.rejectValue("tasks", "NotAvailable", arguments, null);
                    }
                } catch (PortletException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Permission check
        if (!form.isAdmin()) {
            throw new PortletException("Current user is not a global portal administrator.");
        }


        // Delete
        this.repository.delete(portalControllerContext, form);

        
        // Redirection URL
        String redirectionUrl;
        if ("Room".equals(form.getDocument().getType())) {
            CMSObjectPath objectPath = CMSObjectPath.parse(form.getDocument().getPath());
            CMSObjectPath parentObjectPath = objectPath.getParent();
            String parentPath = parentObjectPath.toString();
            redirectionUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, parentPath, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null,
                    null, null, null);
        } else {
            try {
                redirectionUrl = this.portalUrlFactory.getHomePageUrl(portalControllerContext, true);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }
        
        
        // Destroy page URL
        String destroyPageUrl;
        try {
            destroyPageUrl = this.portalUrlFactory.getDestroyCurrentPageUrl(portalControllerContext, redirectionUrl, true);
        } catch (PortalException e) {
            throw new PortletException(e);
        }


        // Internationalization key
        String key;
        if ("Room".equals(form.getDocument().getType())) {
            key = "MESSAGE_WORKSPACE_DELETE_ROOM_SUCCESS";
        } else {
            key = "MESSAGE_WORKSPACE_DELETE_WORKSPACE_SUCCESS";
        }

        // Notification
        String message = bundle.getString(key, form.getTitle());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);

        return destroyPageUrl;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, int index) throws PortletException {
        this.changeActivation(portalControllerContext, form, index, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void show(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, int index) throws PortletException {
        this.changeActivation(portalControllerContext, form, index, true);
    }


    /**
     * Change task activation state.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @param index task index
     * @param active activation indicator
     * @throws PortletException
     */
    private void changeActivation(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, int index, boolean active)
            throws PortletException {
        // Selected task
        Task task = form.getTasks().get(index);

        // Active indicator
        task.setActive(active);
        // Updated indicator
        task.setUpdated(true);
        // Sorted indicator
        task.setSorted(true);

        // Order
        if (active) {
            task.setOrder(form.getTasks().size());
        } else {
            task.setOrder(0);
        }

        // Sort task
        this.sort(portalControllerContext, form);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Workspace path
        String path = form.getDocument().getPath();

        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
