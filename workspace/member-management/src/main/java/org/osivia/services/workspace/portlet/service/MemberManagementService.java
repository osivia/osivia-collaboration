package org.osivia.services.workspace.portlet.service;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.*;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

/**
 * Member management service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MemberManagementService {

    /**
     * Members help location property name.
     */
    String MEMBERS_HELP_LOCATION_PROPERTY = "workspace-member-management.members.help.location";
    /**
     * Invitations help location property name.
     */
    String INVITATIONS_HELP_LOCATION_PROPERTY = "workspace-member-management.invitations.help.location";
    /**
     * Invitation requests help location property name.
     */
    String INVITATION_REQUESTS_HELP_LOCATION_PROPERTY = "workspace-member-management.requests.help.location";
    /**
     * Import CSV help location property name.
     */
    String IMPORT_HELP_LOCATION_PROPERTY = "workspace-member-management.importcsv.help.location";

    /**
     * Select2 results page size.
     */
    int SELECT2_RESULTS_PAGE_SIZE = 6;
    /**
     * Select2 max results.
     */
    int SELECT2_MAX_RESULTS = 100;

    /**
     * File upload max size.
     */
    long FILE_UPLOAD_MAX_SIZE = FileUtils.ONE_MB;


    /**
     * Get options.
     *
     * @param portalControllerContext portal controller context
     * @return options
     */
    MemberManagementOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get members form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    MembersForm getMembersForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort members.
     *
     * @param portalControllerContext portal controller context
     * @param form                    members form
     * @param sort                    sort
     * @param alt                     alternative sort indicator
     */
    void sortMembers(PortalControllerContext portalControllerContext, AbstractMembersForm<? extends MemberObject> form, MembersSort sort, boolean alt)
            throws PortletException;


    /**
     * Remove members.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     * @param identifiers             selected member identifiers
     */
    void removeMembers(PortalControllerContext portalControllerContext, MemberManagementOptions options, MembersForm form, String[] identifiers)
            throws PortletException;


    /**
     * Get members help.
     *
     * @param portalControllerContext portal controller context
     * @return help
     */
    String getMembersHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get members toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected items indexes
     * @return DOM element
     */
    Element getMembersToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException;


    /**
     * Export members in CSV format.
     *
     * @param portalControllerContext portal controller context
     * @param members                 members form
     * @param outputStream            portlet output stream
     */
    void exportMembersCsv(PortalControllerContext portalControllerContext, MembersForm members, OutputStream outputStream) throws PortletException, IOException;


    /**
     * Get invitations form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    InvitationsForm getInvitationsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitations creation form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    InvitationsCreationForm getInvitationsCreationForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Search persons.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param filter                  search filter
     * @param page                    pagination page number
     * @param tokenizer               tokenizer indicator
     * @return JSON object
     */
    JSONObject searchPersons(PortalControllerContext portalControllerContext, MemberManagementOptions options, String filter, int page, boolean tokenizer)
            throws PortletException;


    /**
     * Sort invitations.
     *
     * @param portalControllerContext portal controller context
     * @param form                    invitations form
     * @param sort                    sort
     * @param alt                     alternative sort indicator
     */
    void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, MembersSort sort, boolean alt) throws PortletException;


    /**
     * Update invitations.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    invitations form
     */
    void updateInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException;


    /**
     * Purge invitations history.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    invitations form
     */
    void purgeInvitationsHistory(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException;


    /**
     * Delete invitations.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     * @param identifiers             selected invitation identifiers
     */
    void deleteInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form, String[] identifiers)
            throws PortletException;


    /**
     * Validate invitations creation form.
     *
     * @param errors errors
     * @param form   invitations creation form
     */
    void validateInvitationsCreationForm(Errors errors, InvitationsCreationForm form);


    /**
     * Create invitations.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param invitationsForm         invitations form
     * @param creationForm            invitations creation form
     */
    void createInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm invitationsForm,
                           InvitationsCreationForm creationForm) throws PortletException;


    /**
     * Get invitations help.
     *
     * @param portalControllerContext portal controller context
     * @return help
     */
    String getInvitationsHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitations toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected items indexes
     * @return DOM element
     */
    Element getInvitationsToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException;


    /**
     * Get invitation requests form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    InvitationRequestsForm getInvitationRequestsForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitation requests toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected items indexes
     * @return DOM element
     */
    Element getInvitationRequestsToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException;


    /**
     * Sort invitation requests.
     *
     * @param portalControllerContext portal controller context
     * @param form                    invitation requests form
     * @param sort                    sort
     * @param alt                     alternative sort indicator
     */
    void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, MembersSort sort, boolean alt)
            throws PortletException;


    /**
     * Get invitation requests help.
     *
     * @param portalControllerContext portal controller context
     * @return help
     */
    String getInvitationRequestsHelp(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Accept invitation requests.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     * @param identifiers             invitation request identifiers
     */
    void acceptInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form,
                                  String[] identifiers) throws PortletException;


    /**
     * Decline invitation requests.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     * @param identifiers             invitation request identifiers
     */
    void declineInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form,
                                   String[] identifiers) throws PortletException;


    /**
     * Get invitation edition form.
     *
     * @param portalControllerContext portal controller context
     * @param path                    invitation document path
     * @return form
     */
    InvitationEditionForm getInvitationEditionForm(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Resend invitation.
     *
     * @param portalControllerContext portal controller context
     * @param form                    invitation edition form
     */
    void resendInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;


    /**
     * Update invitation.
     *
     * @param portalControllerContext portal controller context
     * @param form                    invitation edition form
     */
    void updateInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;


    /**
     * Delete invitation.
     *
     * @param portalControllerContext portal controller context
     * @param form                    invitation edition form
     */
    void deleteInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException;


    /**
     * Get change role form.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             selected item identifiers
     * @param memberType              member type
     * @param formType                form type
     * @return form
     */
    <M extends MemberObject, F extends AbstractChangeRoleForm<M>> F getChangeRoleForm(PortalControllerContext portalControllerContext, String[] identifiers,
                                                                                      Class<M> memberType, Class<F> formType) throws PortletException;


    /**
     * Update role.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     */
    <M extends MemberObject, F extends AbstractChangeRoleForm<M>> void updateRole(PortalControllerContext portalControllerContext,
                                                                                  MemberManagementOptions options, F form) throws PortletException;


    /**
     * Get add to group form.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             selected item identifiers
     * @param memberType              member type
     * @param formType                form type
     * @return form
     */
    <M extends MemberObject, F extends AbstractAddToGroupForm<M>> F getAddToGroupForm(PortalControllerContext portalControllerContext, String[] identifiers,
                                                                                      Class<M> memberType, Class<F> formType) throws PortletException;


    /**
     * Add to group.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     */
    <M extends MemberObject, F extends AbstractAddToGroupForm<M>> void addToGroup(PortalControllerContext portalControllerContext,
                                                                                  MemberManagementOptions options, F form) throws PortletException;


    /**
     * Get resend invitations form.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             selected invitation identifiers
     * @return form
     */
    ResendInvitationsForm getResendInvitationsForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException;


    /**
     * Resend invitations.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     */
    void resendInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, ResendInvitationsForm form)
            throws PortletException;


    /**
     * Drop invitation workflow.
     *
     * @param portalControllerContext portal controller context
     * @param path                    invitation path
     */
    void dropInvitation(PortalControllerContext portalControllerContext, String path);


    /**
     * Get import form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    ImportForm getImportForm(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Upload import.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     */
    void upload(PortalControllerContext portalControllerContext, MemberManagementOptions options, ImportForm form) throws IllegalStateException, IOException;


    /**
     * Prepare invitations import.
     *
     * @param portalControllerContext portal controller context
     * @param options                 options
     * @param form                    form
     */
    void prepareImportInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options,
                                  ImportForm form) throws ParseException, PortalException, PortletException, IOException;


    /**
     * Prepare help for csv import screen.
     *
     * @param portalControllerContext portal controller context
     * @return help
     */
    String getImportHelp(PortalControllerContext portalControllerContext) throws PortletException;


}
