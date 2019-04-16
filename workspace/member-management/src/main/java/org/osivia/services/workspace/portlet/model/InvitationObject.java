package org.osivia.services.workspace.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Invitation absract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see MemberObject
 */
public abstract class InvitationObject extends MemberObject {

    /** Document. */
    private Document document;
    /** State. */
    private InvitationState state;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public InvitationObject(Person person) {
        super(person);
    }

    /**
     * Constructor.
     * 
     * @param uid user identifier
     */
    public InvitationObject(String uid) {
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

}
