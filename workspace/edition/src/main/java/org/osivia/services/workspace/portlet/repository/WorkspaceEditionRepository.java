package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.CreateTaskForm;
import org.osivia.services.workspace.portlet.model.Task;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;

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
     * Get task document types.
     * 
     * @param portalControllerContext portal controller context
     * @return document types
     * @throws PortletException
     */
    List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException;


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
     * @param createTaskForm create task form
     * @return task
     * @throws PortletException
     */
    Task createTask(PortalControllerContext portalControllerContext, CreateTaskForm createTaskForm) throws PortletException;


    /**
     * Delete workspace.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
