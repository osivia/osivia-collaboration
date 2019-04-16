package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;

/**
 * Edit local group portlet service interface.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsService
 */
public interface EditLocalGroupService extends LocalGroupsService {

    /**
     * Get edit local group form.
     * 
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return form
     * @throws PortletException
     */
    LocalGroupForm getForm(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Edit local group.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void edit(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException;

}
