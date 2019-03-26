package org.osivia.services.workspace.portlet.model;

import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation request java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see InvitationObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationRequest extends InvitationObject {

    /** Accepted invitation request indicator. */
    private boolean accepted;
    /** User message */
    private String userMessage;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public InvitationRequest(Person person) {
        super(person);
    }

    /**
     * Constructor.
     * 
     * @param uid user identifier
     */
    public InvitationRequest(String uid) {
        super(uid);
    }


    /**
     * Getter for accepted.
     * 
     * @return the accepted
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Setter for accepted.
     * 
     * @param accepted the accepted to set
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Getter for userMessage.
     * 
     * @return userMessage
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Setter for userMessage.
     * 
     * @param userMessage
     */
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

}
