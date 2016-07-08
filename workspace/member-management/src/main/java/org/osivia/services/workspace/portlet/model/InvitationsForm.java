package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitations form java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationsForm {

    /** Invitations. */
    private List<Invitation> invitations;


    /**
     * Constructor.
     */
    public InvitationsForm() {
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

}
