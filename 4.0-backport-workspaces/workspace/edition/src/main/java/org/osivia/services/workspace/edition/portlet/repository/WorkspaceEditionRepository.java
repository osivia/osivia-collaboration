package org.osivia.services.workspace.edition.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;

/**
 * Workspace edition repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionRepository {

    /**
     * Get workspace.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get tasks.
     * 
     * @param portalControllerContext portal controller context
     * @param basePath CMS base path
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext, String basePath) throws PortletException;


    /**
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options, WorkspaceEditionForm form) throws PortletException;


    /**
     * Create task.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace task creation form
     * @return created task
     * @throws PortletException
     */
    Task createTask(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException;


    /**
     * Delete workspace.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException;

}
