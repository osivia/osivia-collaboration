package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;

/**
 * ACL entries java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class AclEntries {

    /** ACL entries. */
    private List<AclEntry> entries;
    /** Workspace identifier. */
    private String workspaceId;
    /** Document. */
    private Document document;
    /** Inherited ACL entries indicator. */
    private boolean inherited;


    /**
     * Constructor.
     */
    public AclEntries() {
        super();
    }


    /**
     * Getter for entries.
     *
     * @return the entries
     */
    public List<AclEntry> getEntries() {
        return this.entries;
    }

    /**
     * Setter for entries.
     *
     * @param entries the entries to set
     */
    public void setEntries(List<AclEntry> entries) {
        this.entries = entries;
    }

    /**
     * Getter for workspaceId.
     *
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return this.workspaceId;
    }

    /**
     * Setter for workspaceId.
     *
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Getter for document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return this.document;
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
     * Getter for inherited.
     *
     * @return the inherited
     */
    public boolean isInherited() {
        return this.inherited;
    }

    /**
     * Setter for inherited.
     *
     * @param inherited the inherited to set
     */
    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

}
