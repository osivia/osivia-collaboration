package org.osivia.services.workspace.model;

/**
 * Workspace creation form java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class WorkspaceCreationForm {

    /** Workspace name. */
    private String name;
    /** Workspace description. */
    private String description;


    /**
     * Constructor.
     */
    public WorkspaceCreationForm() {
        super();
    }


    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
