package org.osivia.services.workspace.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
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
    /** Role. */
    private WorkspaceRole role;
    /** State. */
    private InvitationState state;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public Invitation(Person person) {
        super(person);
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
