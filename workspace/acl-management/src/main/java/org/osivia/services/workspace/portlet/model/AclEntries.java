package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ACL entries java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AclEntries {

    /** Local public ACL entry indicator. */
    private boolean publicEntry;
    /** Local public ACL entry indicator original value. */
    private boolean publicEntryOriginal;
    /** Inherited ACL entries indicator. */
    private boolean inherited;
    /** Inherited ACL entries indicator original value. */
    private boolean inheritedOriginal;
    /** Public inheritance indicator. */
    private boolean publicInheritance;
    /** ACL entries. */
    private List<AclEntry> entries;
    /** Modified ACL entries indicator. */
    private boolean modified;
    /** Saved user ACL entry. */
    private AclEntry savedUserEntry;
    /** Workspace identifier. */
    private String workspaceId;
    /** Document. */
    private Document document;
    /** Display controls indicator. */
    private boolean displayControls;


    /**
     * Constructor.
     */
    public AclEntries() {
        super();
    }


    /**
     * Getter for publicEntry.
     * 
     * @return the publicEntry
     */
    public boolean isPublicEntry() {
        return publicEntry;
    }

    /**
     * Setter for publicEntry.
     * 
     * @param publicEntry the publicEntry to set
     */
    public void setPublicEntry(boolean publicEntry) {
        this.publicEntry = publicEntry;
    }

    /**
     * Getter for publicEntryOriginal.
     * 
     * @return the publicEntryOriginal
     */
    public boolean isPublicEntryOriginal() {
        return publicEntryOriginal;
    }

    /**
     * Setter for publicEntryOriginal.
     * 
     * @param publicEntryOriginal the publicEntryOriginal to set
     */
    public void setPublicEntryOriginal(boolean publicEntryOriginal) {
        this.publicEntryOriginal = publicEntryOriginal;
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

    /**
     * Getter for inheritedOriginal.
     * 
     * @return the inheritedOriginal
     */
    public boolean isInheritedOriginal() {
        return inheritedOriginal;
    }

    /**
     * Setter for inheritedOriginal.
     * 
     * @param inheritedOriginal the inheritedOriginal to set
     */
    public void setInheritedOriginal(boolean inheritedOriginal) {
        this.inheritedOriginal = inheritedOriginal;
    }

    /**
     * Getter for publicInheritance.
     * 
     * @return the publicInheritance
     */
    public boolean isPublicInheritance() {
        return publicInheritance;
    }

    /**
     * Setter for publicInheritance.
     * 
     * @param publicInheritance the publicInheritance to set
     */
    public void setPublicInheritance(boolean publicInheritance) {
        this.publicInheritance = publicInheritance;
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
     * Getter for modified.
     * 
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }


    /**
     * Setter for modified.
     * 
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }


    /**
     * Getter for savedUserEntry.
     * 
     * @return the savedUserEntry
     */
    public AclEntry getSavedUserEntry() {
        return savedUserEntry;
    }

    /**
     * Setter for savedUserEntry.
     * 
     * @param savedUserEntry the savedUserEntry to set
     */
    public void setSavedUserEntry(AclEntry savedUserEntry) {
        this.savedUserEntry = savedUserEntry;
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
     * Getter for displayControls.
     * 
     * @return the displayControls
     */
    public boolean isDisplayControls() {
        return displayControls;
    }

    /**
     * Setter for displayControls.
     * 
     * @param displayControls the displayControls to set
     */
    public void setDisplayControls(boolean displayControls) {
        this.displayControls = displayControls;
    }

}
