package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AddMembersToLocalGroupsForm;

/**
 * Add members to local groups portlet service interface.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsService
 */
public interface AddMembersToLocalGroupsService extends LocalGroupsService {

    /**
     * Get add members to local groups form.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selection identifiers
     * @return form
     * @throws PortletException
     */
    AddMembersToLocalGroupsForm getForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException;


    /**
     * Add members to local groups.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void addMembers(PortalControllerContext portalControllerContext, AddMembersToLocalGroupsForm form) throws PortletException;

}
