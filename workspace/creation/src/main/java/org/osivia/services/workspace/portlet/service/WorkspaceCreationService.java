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
     * Check workspace title availability.
     * 
     * @param portalControllerContext portal controller context
     * @param modelWebId model webId
     * @param procedureInstanceUuid procedure instance UUID
     * @param title workspace title
     * @param titleVariableName workspace title variable name
     * @return true if workspace title is available
     * @throws PortletException
     */
    boolean checkTitleAvailability(PortalControllerContext portalControllerContext, String modelWebId, String procedureInstanceUuid, String title,
            String titleVariableName) throws PortletException;


    /**
     * Create workspace.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException;

}
