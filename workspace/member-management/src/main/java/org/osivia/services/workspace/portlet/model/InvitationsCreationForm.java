package org.osivia.services.workspace.portlet.model;

import java.util.List;

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

    /** Invitations. */
    private List<Invitation> invitations;
    /** Role. */
    private WorkspaceRole role;
    /** Warning indicator. */
    private boolean warning;


    /**
     * Constructor.
     */
    public InvitationsCreationForm() {
        super();
    }


    /**
     * Getter for invitations.
     * 
     * @return the invitations
     */
    public List<Invitation> getInvitations() {
        return invitations;
    }

    /**
     * Setter for invitations.
     * 
     * @param invitations the invitations to set
     */
    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
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
     * Getter for warning.
     * 
     * @return the warning
     */
    public boolean isWarning() {
        return warning;
    }

    /**
     * Setter for warning.
     * 
     * @param warning the warning to set
     */
    public void setWarning(boolean warning) {
        this.warning = warning;
    }

}
