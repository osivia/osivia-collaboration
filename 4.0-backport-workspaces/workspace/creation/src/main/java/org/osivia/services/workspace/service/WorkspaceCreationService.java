package org.osivia.services.workspace.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.model.WorkspaceCreationForm;

/**
 * Workspace creation service.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceCreationService {

    /**
     * Create workspace.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException;

}
