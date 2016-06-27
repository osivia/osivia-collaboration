package org.osivia.services.workspace.edition.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;

/**
 * Workspace edition repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionRepository {

    /**
     * Get workspace edtion form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


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
     * @param form workspace edition form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
