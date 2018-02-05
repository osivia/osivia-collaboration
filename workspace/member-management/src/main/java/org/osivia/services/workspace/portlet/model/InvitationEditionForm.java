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
    /** Message. */
    private String message;

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

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

}
