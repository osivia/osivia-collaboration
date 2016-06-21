package org.osivia.services.workspace.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Workspace map repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface WorkspaceMapRepository {

    /**
     * Get workspace path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getWorkspacePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get navigation path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getNavigationPath(PortalControllerContext portalControllerContext) throws PortletException;

}
