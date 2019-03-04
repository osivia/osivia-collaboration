package org.osivia.services.workspace.portlet.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AbstractMembersForm;
import org.osivia.services.workspace.portlet.model.AddToGroupForm;
import org.osivia.services.workspace.portlet.model.ChangeRoleForm;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.MembersSort;
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
    /** Select2 max results. */
    int SELECT2_MAX_RESULTS = 100;


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
     * @param sort sort
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortMembers(PortalControllerContext portalControllerContext, AbstractMembersForm form, MembersSort sort, boolean alt) throws PortletException;


    /**
     * Remove members.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form form
     * @param identifiers selected member identifiers
     * @throws PortletException
     */
    void removeMembers(PortalControllerContext portalControllerContext, MemberManagementOptions options, MembersForm form, String[] identifiers)
            throws PortletException;


    /**
     * Get members help.
     * 
     * @param portalControllerContext portal controller context
     * @return help
     * @throws PortletException
     */
    String getMembersHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get members toolbar DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param indexes selected items indexes
     * @return DOM element
     * @throws PortletException
     */
    Element getMembersToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException;


    /**
     * Export members in CSV format.
     * 
     * @param portalControllerContext portal controller context
     * @param members members form
     * @param outputStream portlet output stream
     * @throws PortletException
     * @throws IOException
     */
    void exportMembersCsv(PortalControllerContext portalControllerContext, MembersForm members, OutputStream outputStream) throws PortletException, IOException;


    /**
     * Get change role form.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected member identifiers
     * @return form
     * @throws PortletException
     */
    ChangeRoleForm getChangeRoleForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException;


    /**
     * Update role.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form form
     * @throws PortletException
     */
    void updateRole(PortalControllerContext portalControllerContext, MemberManagementOptions options, ChangeRoleForm form) throws PortletException;


    /**
     * Get add to group form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    AddToGroupForm getAddToGroupForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Add to group.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form form
     * @throws PortletException
     */
    void addToGroup(PortalControllerContext portalControllerContext, MemberManagementOptions options, AddToGroupForm form) throws PortletException;


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
     * @param sort sort
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, MembersSort sort, boolean alt) throws PortletException;


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
     * Purge invitations history.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form invitations form
     * @throws PortletException
     */
    void purgeInvitationsHistory(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
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
     * @param sort sort
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, MembersSort sort, boolean alt)
            throws PortletException;


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


    /**
     * Get invitation edition form.
     * 
     * @param portalControllerContext portal controller context
     * @param path invitation document path
     * @return form
     * @throws PortletException
     */
    InvitationEditionForm getInvitationEditionForm(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Resend invitation.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitation edition form
     * @throws PortletException
     */
    void resendInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;


    /**
     * Update invitation.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitation edition form
     * @throws PortletException
     */
    void updateInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;


    /**
     * Delete invitation.
     * 
     * @param portalControllerContext portal controller context
     * @param form invitation edition form
     * @throws PortletException
     */
    void deleteInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;
    
}
