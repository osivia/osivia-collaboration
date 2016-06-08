package org.osivia.services.workspace.portlet.repository.impl;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
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

        // Tasks
        List<TaskbarTask> navigationTasks;
        try {
            navigationTasks = this.taskbarService.getNavigationTasks(portalControllerContext, basePath);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        List<Task> tasks = new ArrayList<Task>(navigationTasks.size());
        int order = 1;
        for (TaskbarTask navigationTask : navigationTasks) {
            Task task = new Task(navigationTask);

            // Display name
            String displayName;
            if (navigationTask.getKey() != null) {
                displayName = bundle.getString(navigationTask.getKey(), navigationTask.getCustomizedClassLoader());
            } else {
                displayName = navigationTask.getTitle();
            }
            task.setDisplayName(displayName);

            // Order
            if (!navigationTask.isDisabled()) {
                task.setActive(!navigationTask.isDisabled());
                task.setOrder(order++);
            }

            tasks.add(task);
        }

        // Form
        WorkspaceEditionForm form = new WorkspaceEditionForm();
        form.setPath(workspace.getPath());
        form.setTitle(workspace.getTitle());
        form.setDescription(workspace.getString("dc:description"));
        form.setTasks(tasks);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = new WorkspaceEditionCommand(form);
        nuxeoController.executeNuxeoCommand(command);
    }

}
