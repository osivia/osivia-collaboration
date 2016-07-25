package org.osivia.services.workspace.portlet.repository;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;

/**
 * Member management repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface MemberManagementRepository {

    /** Model identifier. */
    String MODEL_ID = "invitation";

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


    /**
     * Get workspace identifier.
     * 
     * @param portalControllerContext portal controller context
     * @return workspace identifier
     * @throws PortletException
     */
    String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException;


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
     * @return invitations
     * @throws PortletException
     */
    List<Invitation> getInvitations(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Search persons.
     * 
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @return persons
     * @throws PortletException
     */
    List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter) throws PortletException;


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
     * @param pending pending invitations indicator
     * @throws PortletException
     */
    void updateInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations, boolean pending)
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
     * Accept invitation.
     * 
     * @param portalControllerContext portal controller context
     * @param variables task variables
     */
    void acceptInvitation(PortalControllerContext portalControllerContext, Map<String, String> variables);

}
