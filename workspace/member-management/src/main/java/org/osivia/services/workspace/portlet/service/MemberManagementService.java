package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.springframework.validation.Errors;

import net.sf.json.JSONObject;

/**
 * Member management service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface MemberManagementService {

    /** Members help location property name. */
    String MEMBERS_HELP_LOCATION_PROPERTY = "workspace-member-management.members.help.location";
    /** Invitations help location property name. */
    String INVITATIONS_HELP_LOCATION_PROPERTY = "workspace-member-management.invitations.help.location";
    /** Invitation requests help location property name. */
    String INVITATION_REQUESTS_HELP_LOCATION_PROPERTY = "workspace-member-management.requests.help.location";

    /** Select2 results page size. */
    int SELECT2_RESULTS_PAGE_SIZE = 6;


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
     * Get members help.
     * 
     * @param portalControllerContext portal controller context
     * @return help
     * @throws PortletException
     */
    String getMembersHelp(PortalControllerContext portalControllerContext) throws PortletException;


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
     * @param options options
     * @param filter search filter
     * @param page pagination page number
     * @param tokenizer tokenizer indicator
     * @return JSON object
     * @throws PortletException
     */
    JSONObject searchPersons(PortalControllerContext portalControllerContext, MemberManagementOptions options, String filter, int page, boolean tokenizer)
            throws PortletException;


    /**
     * Sort invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitations form
     * @param sort sort property
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, String sort, boolean alt) throws PortletException;


    /**
     * Update invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form invitations form
     * @throws PortletException
     */
    void updateInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException;


    /**
     * Validate invitations creation form.
     * 
     * @param errors errors
     * @param form invitations creation form
     */
    void validateInvitationsCreationForm(Errors errors, InvitationsCreationForm form);


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


    /**
     * Get invitations help.
     * 
     * @param portalControllerContext portal controller context
     * @return help
     * @throws PortletException
     */
    String getInvitationsHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitation requests form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    InvitationRequestsForm getInvitationRequestsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort invitation requests.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitation requests form
     * @param sort sort property
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, String sort, boolean alt) throws PortletException;


    /**
     * Update invitation requests.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form invitation requests form
     * @throws PortletException
     */
    void updateInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form)
            throws PortletException;


    /**
     * Get invitation requests help.
     * 
     * @param portalControllerContext portal controller context
     * @return help
     * @throws PortletException
     */
    String getInvitationRequestsHelp(PortalControllerContext portalControllerContext) throws PortletException;

}
