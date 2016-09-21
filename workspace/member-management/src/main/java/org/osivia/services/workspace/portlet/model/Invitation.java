package org.osivia.services.workspace.portlet.model;

import java.util.Date;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see MemberObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Invitation extends MemberObject {

    /** Document. */
    private Document document;
    /** State. */
    private InvitationState state;
    /** Acknowledgment date. */
    private Date acknowledgmentDate;

    /** Unknown user indicator. */
    private final boolean unknownUser;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public Invitation(Person person) {
        super(person);
        this.unknownUser = false;
    }

    /**
     * Constructor used when no person was found with this UID.
     * 
     * @param uid person UID
     */
    public Invitation(String uid) {
        super(uid);
        this.unknownUser = true;
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
     * Getter for acknowledgmentDate.
     * 
     * @return the acknowledgmentDate
     */
    public Date getAcknowledgmentDate() {
        return acknowledgmentDate;
    }

    /**
     * Setter for acknowledgmentDate.
     * 
     * @param acknowledgmentDate the acknowledgmentDate to set
     */
    public void setAcknowledgmentDate(Date acknowledgmentDate) {
        this.acknowledgmentDate = acknowledgmentDate;
    }

    /**
     * Getter for unknownUser.
     * 
     * @return the unknownUser
     */
    public boolean isUnknownUser() {
        return unknownUser;
    }

}
