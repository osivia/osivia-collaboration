package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;

/**
 * Workspace creation service.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceCreationService {

    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    WorkspaceCreationForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Create workspace.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException;

}
