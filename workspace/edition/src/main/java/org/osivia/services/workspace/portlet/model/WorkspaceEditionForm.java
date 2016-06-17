package org.osivia.services.workspace.portlet.model;

import java.util.List;

/**
 * Workspace edition form java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class WorkspaceEditionForm {

    /** Workspace path. */
    private String path;
    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace tasks. */
    private List<Task> tasks;
    /** Workspace type. */
    private String type;


    /**
     * Constructor.
     */
    public WorkspaceEditionForm() {
        super();
    }


    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setter for path.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
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

    /**
     * Getter for tasks.
     * 
     * @return the tasks
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Setter for tasks.
     * 
     * @param tasks the tasks to set
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
