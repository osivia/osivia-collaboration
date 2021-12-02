package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitations creation form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationsCreationForm {

    /** Pending invitations. */
    private List<Invitation> pendingInvitations;
    /** Role. */
    private WorkspaceRole role;
    /** Local groups. */
    private List<CollabProfile> localGroups;
    /** Message. */
    private String message;


    /**
     * Constructor.
     */
    public InvitationsCreationForm() {
        super();
    }


    /**
     * Getter for pendingInvitations.
     * 
     * @return the pendingInvitations
     */
    public List<Invitation> getPendingInvitations() {
        return pendingInvitations;
    }

    /**
     * Setter for pendingInvitations.
     * 
     * @param pendingInvitations the pendingInvitations to set
     */
    public void setPendingInvitations(List<Invitation> pendingInvitations) {
        this.pendingInvitations = pendingInvitations;
    }

    /**
     * Getter for role.
     * 
     * @return the role
     */
    public WorkspaceRole getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    /**
     * Getter for localGroups.
     * 
     * @return the localGroups
     */
    public List<CollabProfile> getLocalGroups() {
        return localGroups;
    }

    /**
     * Setter for localGroups.
     * 
     * @param localGroups the localGroups to set
     */
    public void setLocalGroups(List<CollabProfile> localGroups) {
        this.localGroups = localGroups;
    }

    /**
     * Getter for message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
