package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Resend invitations form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMembersForm
 * @see Invitation
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResendInvitationsForm extends AbstractMembersForm<Invitation> {

    /** Message. */
    private String message;


    /**
     * Constructor.
     */
    public ResendInvitationsForm() {
        super();
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
