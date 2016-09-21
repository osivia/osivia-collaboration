package org.osivia.services.workspace.portlet.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;

/**
 * Member management repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface MemberManagementRepository {

    /** Invitation model identifier. */
    String INVITATION_MODEL_ID = "invitation";
    /** Request model identifier. */
    String REQUEST_MODEL_ID = "invitation-request";

    /** Workspace identifier property. */
    String WORKSPACE_IDENTIFIER_PROPERTY = "workspaceId";
    /** Workspace title property. */
    String WORKSPACE_TITLE_PROPERTY = "workspaceTitle";
    /** Person UID property. */
    String PERSON_UID_PROPERTY = "uid";
    /** Invitation state property. */
    String INVITATION_STATE_PROPERTY = "invitationState";
    /** Role property. */
    String ROLE_PROPERTY = "role";
    /** Acknowledgment date property. */
    String ACKNOWLEDGMENT_DATE_PROPERTY = "acknowledgmentDate";
    /** New user indicator property. */
    String NEW_USER_PROPERTY = "newUser";
    /** Generated password property. */
    String GENERATED_PASSWORD_PROPERTY = "generatedPassword";


    /**
     * Get current workspace identifier.
     * 
     * @param portalControllerContext portal controller context
     * @return workspace identifier
     * @throws PortletException
     */
    String getCurrentWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get invitations count.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return invitations count
     * @throws PortletException
     */
    int getInvitationsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Get requests count.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return requests count
     * @throws PortletException
     */
    int getRequestsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Get roles.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return roles
     * @throws PortletException
     */
    List<WorkspaceRole> getRoles(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Get members.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return members
     * @throws PortletException
     */
    List<Member> getMembers(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Update member.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param member member
     * @throws PortletException
     */
    void updateMember(PortalControllerContext portalControllerContext, String workspaceId, Member member) throws PortletException;


    /**
     * Get invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param memberIdentifiers member identifiers
     * @return invitations
     * @throws PortletException
     */
    List<Invitation> getInvitations(PortalControllerContext portalControllerContext, String workspaceId, Set<String> memberIdentifiers) throws PortletException;


    /**
     * Search persons.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @param tokenizer tokenizer indicator
     * @return persons
     * @throws PortletException
     */
    List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter, boolean tokenizer) throws PortletException;


    /**
     * Get person.
     * 
     * @param portalControllerContext portal controller context
     * @param uid person UID
     * @return person
     * @throws PortletException
     */
    Person getPerson(PortalControllerContext portalControllerContext, String uid) throws PortletException;


    /**
     * Update invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param invitations invitations
     * @throws PortletException
     */
    void updateInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations)
            throws PortletException;


    /**
     * Create invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param invitations invitations
     * @param form invitations creation form
     * @return true if at least one invitation was created
     * @throws PortletException
     */
    boolean createInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations, InvitationsCreationForm form)
            throws PortletException;


    /**
     * Get invitation requests.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return invitation requests
     * @throws PortletException
     */
    List<InvitationRequest> getInvitationRequests(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Update invitation requests.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param invitationRequests invitation requests
     * @throws PortletException
     */
    void updateInvitationRequests(PortalControllerContext portalControllerContext, String workspaceId, List<InvitationRequest> invitationRequests)
            throws PortletException;


    /**
     * Get help content.
     * 
     * @param portalControllerContext portal controller context
     * @param property help location property
     * @return help content
     * @throws PortletException
     */
    String getHelp(PortalControllerContext portalControllerContext, String property) throws PortletException;


    /**
     * Accept invitation.
     * 
     * @param portalControllerContext portal controller context
     * @param variables task variables
     */
    void acceptInvitation(PortalControllerContext portalControllerContext, Map<String, String> variables);


    /**
     * Check if a pending invitation exists.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param uid user identifier
     * @param invitationRequest invitation request indicator
     * @return true if a pending invitation exists
     * @throws PortletException
     */
    boolean isPendingInvitation(PortalControllerContext portalControllerContext, String workspaceId, String uid, boolean invitationRequest) throws PortletException;


    /**
     * Create invitation request.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param uid user identifier
     * @throws PortletException
     */
    void createInvitationRequest(PortalControllerContext portalControllerContext, String workspaceId, String uid) throws PortletException;

}
