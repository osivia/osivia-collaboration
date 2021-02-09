package org.osivia.services.workspace.portlet.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberObject;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

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
    /** Workspace path property. */
    String WORKSPACE_PATH_PROPERTY = "documentPath";
    /** Workspace title property. */
    String WORKSPACE_TITLE_PROPERTY = "workspaceTitle";
    /** Person UID property. */
    String PERSON_UID_PROPERTY = "uid";
    /** Initiator property. */
    String INITIATOR_PROPERTY = "initiator";    
    /** Invitation state property. */
    String INVITATION_STATE_PROPERTY = "invitationState";
    /** Message sent by the user. */
    String USER_MESSAGE = "userMessage";    
    /** Role property. */
    String ROLE_PROPERTY = "role";
    /** Invitation local groups property. */
    String INVITATION_LOCAL_GROUPS_PROPERTY = "invitationLocalGroups";
    /** Invitation message property. */
    String INVITATION_MESSAGE_PROPERTY = "invitationMessage";
    /** Acknowledgment date property. */
    String ACKNOWLEDGMENT_DATE_PROPERTY = "acknowledgmentDate";
    /** Invitation resending date property. */
    String INVITATION_RESENDING_DATE = "invitationResendingDate";
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
     * Get current workspace type.
     * 
     * @param portalControllerContext portal controller context
     * @return workspace type
     * @throws PortletException
     */
    WorkspaceType getCurrentWorkspaceType(PortalControllerContext portalControllerContext) throws PortletException;


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
     * Get workspace local groups.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return groups
     * @throws PortletException
     */
    List<CollabProfile> getLocalGroups(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


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
     * Add members to local group.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param members members
     * @param group local group
     * @throws PortletException
     */
    void addToGroup(PortalControllerContext portalControllerContext, String workspaceId, List<MemberObject> members, CollabProfile group)
            throws PortletException;


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
     * @param invitations invitations
     * @throws PortletException
     */
    void updateInvitations(PortalControllerContext portalControllerContext, List<Invitation> invitations) throws PortletException;


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
     * @param memberIdentifiers member identifiers
     * @return invitation requests
     * @throws PortletException
     */
    List<InvitationRequest> getInvitationRequests(PortalControllerContext portalControllerContext, String workspaceId, Set<String> memberIdentifiers)
            throws PortletException;


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
     * @param nuxeoController Nuxeo controller
     * @param variables task variables
     */
    void acceptInvitation(NuxeoController nuxeoController, Map<String, String> variables);


    /**
     * Get pending invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param uid user identifier
     * @return Nuxeo documents
     * @throws PortletException
     */
    List<Document> getPendingInvitations(PortalControllerContext portalControllerContext, String uid) throws PortletException;


    /**
     * Create invitation request.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param uid user identifier
     * @param userMessage user message input
     * @throws PortletException
     */
    void createInvitationRequest(PortalControllerContext portalControllerContext, String workspaceId, String uid, String userMessage) throws PortletException;


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
     * @param resendingDate invitation resending date
     * @throws PortletException
     */
    void resendInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form, Date resendingDate) throws PortletException;


    /**
     * Resend invitations.
     * 
     * @param portalControllerContext portal controller context
     * @param invitations invitations
     * @param message invitations resending message
     * @param date invitations resending date
     * @return true if the invitations were correctly resent
     * @throws PortletException
     */
    boolean resendInvitations(PortalControllerContext portalControllerContext, List<Invitation> invitations, String message, Date date) throws PortletException;


    /**
     * Drop invitation workflow
     * 
     * @param portalControllerContext
     * @param invitationPath
     */
	void dropInvitation(PortalControllerContext portalControllerContext, String invitationPath);


	/**
	 * Get current workspace document
	 * 
	 * @param portalControllerContext
	 * @return
	 */
	Document getCurrentWorkspace(PortalControllerContext portalControllerContext);

}
