package org.osivia.services.workspace.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
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
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
