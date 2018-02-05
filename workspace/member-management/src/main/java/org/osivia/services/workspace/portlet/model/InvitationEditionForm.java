package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationEditionForm {

    /** Invitation. */
    private Invitation invitation;

    /** Invitation path. */
    private final String path;


    /**
     * Constructor.
     * 
     * @param path invitation path
     */
    public InvitationEditionForm(String path) {
        super();
        this.path = path;
    }


    /**
     * Getter for invitation.
     * 
     * @return the invitation
     */
    public Invitation getInvitation() {
        return invitation;
    }

    /**
     * Setter for invitation.
     * 
     * @param invitation the invitation to set
     */
    public void setInvitation(Invitation invitation) {
        this.invitation = invitation;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

}
