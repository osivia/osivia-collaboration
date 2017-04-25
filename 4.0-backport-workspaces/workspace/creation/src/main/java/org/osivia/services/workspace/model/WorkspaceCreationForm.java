package org.osivia.services.workspace.model;

/**
 * Workspace creation form java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class WorkspaceCreationForm {

    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;


    /**
     * Constructor.
     */
    public WorkspaceCreationForm() {
        super();
    }


    /**
     * Getter for title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
