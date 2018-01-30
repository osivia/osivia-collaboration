package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.LocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroupEditionForm;
import org.osivia.services.workspace.portlet.model.LocalGroups;

/**
 * Workspace local group management service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface LocalGroupManagementService {

    /**
     * Get local groups.
     *
     * @param portalControllerContext portal controller context
     * @return local groups
     * @throws PortletException
     */
    LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get add local group form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    LocalGroup getAddLocalGroupForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get local group edition form from its identifier.
     *
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return form
     * @throws PortletException
     */
    LocalGroupEditionForm getLocalGroupEditionForm(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Save local groups.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @throws PortletException
     */
    void saveLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException;


    /**
     * Create local group.
     *
     * @param portalControllerContext portal controller context
     * @param localGroups local groups
     * @param form local group creation form
     * @throws PortletException
     */
    void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException;


    /**
     * Add member to local group.
     *
     * @param portalControllerContext portal controller context
     * @param form local group edition form
     * @throws PortletException
     */
    void addMember(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException;


    /**
     * Save local group.
     *
     * @param portalControllerContext portal controller context
     * @param form local group edition form
     * @throws PortletException
     */
    void saveLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException;


    /**
     * Delete local group.
     *
     * @param portalControllerContext portal controller context
     * @param form local group edition form
     * @throws PortletException
     */
    void deleteLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException;

}
