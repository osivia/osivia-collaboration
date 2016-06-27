package org.osivia.services.workspace.task.creation.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Workspace task creation repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface WorkspaceTaskCreationRepository {

    /** Workspace type window property. */
    String WORKSPACE_TYPE_WINDOW_PROPERTY = "workspace.type";


    /**
     * Get task document types.
     * 
     * @param portalControllerContext portal controller context
     * @return document types
     * @throws PortletException
     */
    List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException;

}
