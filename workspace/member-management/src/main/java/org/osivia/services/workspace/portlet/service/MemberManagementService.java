package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;

import net.sf.json.JSONArray;

/**
 * Member management service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface MemberManagementService {

    /**
     * Get options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    MemberManagementOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get members form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    MembersForm getMembersForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort members.
     * 
     * @param portalControllerContext portal controller context
     * @param form members form
     * @param sort sort property
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortMembers(PortalControllerContext portalControllerContext, MembersForm form, String sort, boolean alt) throws PortletException;


    /**
     * Update members.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form members form
     * @throws PortletException
     */
    void updateMembers(PortalControllerContext portalControllerContext, MemberManagementOptions options, MembersForm form) throws PortletException;


    /**
     * Get invitations form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    InvitationsForm getInvitationsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitations creation form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    InvitationsCreationForm getInvitationsCreationForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Search persons.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @return JSON array
     * @throws PortletException
     */
    JSONArray searchPersons(PortalControllerContext portalControllerContext, String filter) throws PortletException;


    /**
     * Sort invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitations form
     * @param sort sort property
     * @param alt alternative sort indicator
     * @param sort2 history sort property
     * @param alt2 history alternative sort indicator
     * @throws PortletException
     */
    void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, String sort, boolean alt, String sort2, boolean alt2)
            throws PortletException;


    /**
     * Update pending invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form invitations form
     * @throws PortletException
     */
    void updatePendingInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException;


    /**
     * Update history invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form invitations form
     * @throws PortletException
     */
    void updateHistoryInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException;


    /**
     * Create invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param invitationsForm invitations form
     * @param creationForm invitations creation form
     * @throws PortletException
     */
    void createInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm invitationsForm,
            InvitationsCreationForm creationForm) throws PortletException;

}
