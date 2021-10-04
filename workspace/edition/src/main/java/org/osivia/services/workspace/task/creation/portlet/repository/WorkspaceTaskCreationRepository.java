package org.osivia.services.workspace.task.creation.portlet.repository;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.task.creation.portlet.model.TaskCreationForm;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Workspace task creation repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceTaskCreationRepository {

    /**
     * Workspace path window property.
     */
    String WORKSPACE_PATH_WINDOW_PROPERTY = "workspace.path";
    /**
     * Workspace type window property.
     */
    String WORKSPACE_TYPE_WINDOW_PROPERTY = "workspace.type";


    /**
     * Get task document types.
     *
     * @param portalControllerContext portal controller context
     * @return document types
     */
    List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get Workspace path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getWorkspacePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Create task.
     *
     * @param portalControllerContext portal controller context
     * @param form                    task creation form
     * @return created task path
     */
    String create(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException;

}
