package org.osivia.services.workspace.portlet.model;

import java.util.SortedMap;

import org.osivia.portal.api.cms.DocumentType;


/**
 * Create task form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public class CreateTaskForm {

    /** Created task title. */
    private String createdTitle;
    /** Created task description. */
    private String createdDescription;
    /** Created task document type. */
    private String createdType;
    /** Document types. */
    private SortedMap<String, DocumentType> types;
    /** Has errors indicator. */
    private boolean hasErrors;

    
    /**
     * Constructor.
     */
    public CreateTaskForm() {
        super();
    }


    /**
     * Getter for createdTitle.
     * 
     * @return the createdTitle
     */
    public String getCreatedTitle() {
        return createdTitle;
    }

    /**
     * Setter for createdTitle.
     * 
     * @param createdTitle the createdTitle to set
     */
    public void setCreatedTitle(String createdTitle) {
        this.createdTitle = createdTitle;
    }

    /**
     * Getter for createdDescription.
     * 
     * @return the createdDescription
     */
    public String getCreatedDescription() {
        return createdDescription;
    }

    /**
     * Setter for createdDescription.
     * 
     * @param createdDescription the createdDescription to set
     */
    public void setCreatedDescription(String createdDescription) {
        this.createdDescription = createdDescription;
    }

    /**
     * Getter for createdType.
     * 
     * @return the createdType
     */
    public String getCreatedType() {
        return createdType;
    }

    /**
     * Setter for createdType.
     * 
     * @param createdType the createdType to set
     */
    public void setCreatedType(String createdType) {
        this.createdType = createdType;
    }

    /**
     * Getter for types.
     * 
     * @return the types
     */
    public SortedMap<String, DocumentType> getTypes() {
        return types;
    }

    /**
     * Setter for types.
     * 
     * @param types the types to set
     */
    public void setTypes(SortedMap<String, DocumentType> types) {
        this.types = types;
    }

    /**
     * Getter for hasErrors.
     * 
     * @return the hasErrors
     */
    public boolean isHasErrors() {
        return hasErrors;
    }

    /**
     * Setter for hasErrors.
     * 
     * @param hasErrors the hasErrors to set
     */
    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

}
