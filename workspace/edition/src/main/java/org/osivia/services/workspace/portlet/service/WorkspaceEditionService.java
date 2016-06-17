package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.CreateTaskForm;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;

/**
 * Workspace edition service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionService {

    /**
     * Get workspace edition form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get create task form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    CreateTaskForm getCreateTaskForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort tasks.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Create task.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param createTaskForm create task form
     * @throws PortletException
     */
    void createTask(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, CreateTaskForm createTaskForm) throws PortletException;


    /**
     * Delete workspace and return redirection URL.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @return URL
     * @throws PortletException
     */
    String delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Get workspace URL.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @return URL
     * @throws PortletException
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
