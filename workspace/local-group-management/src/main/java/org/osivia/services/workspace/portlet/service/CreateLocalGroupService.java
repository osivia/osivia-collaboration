package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;

/**
 * Create local group portlet service interface.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsService
 */
public interface CreateLocalGroupService extends LocalGroupsService {

    /**
     * Get create local group form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    LocalGroupForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Create local group.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void create(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException;

}
