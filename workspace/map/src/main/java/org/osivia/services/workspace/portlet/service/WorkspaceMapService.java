package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.WorkspaceMapOptions;

/**
 * Workspace map service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface WorkspaceMapService {

    /**
     * Get workspace map options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    WorkspaceMapOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;

}
