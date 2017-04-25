package org.osivia.services.workspace.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation request java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see InvitationRequest
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationRequest extends MemberObject {

    /** Document. */
    private Document document;
    /** State. */
    private InvitationState state;
    /** Accepted invitation request indicator. */
    private boolean accepted;


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
     * Getter for document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Setter for document.
     * 
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Getter for state.
     * 
     * @return the state
     */
    public InvitationState getState() {
        return state;
    }

    /**
     * Setter for state.
     * 
     * @param state the state to set
     */
    public void setState(InvitationState state) {
        this.state = state;
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

}
