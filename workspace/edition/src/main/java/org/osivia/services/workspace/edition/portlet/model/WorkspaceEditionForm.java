package org.osivia.services.workspace.edition.portlet.model;

import java.util.List;

import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
public class WorkspaceEditionForm {

    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace type. */
    private WorkspaceType type;
    /** Workspace tasks. */
    private List<Task> tasks;
    /** Workspace task creation form. */
    private TaskCreationForm taskCreationForm;


    /**
     * Constructor.
     */
    public WorkspaceEditionForm() {
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

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public WorkspaceType getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(WorkspaceType type) {
        this.type = type;
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
     * Getter for taskCreationForm.
     *
     * @return the taskCreationForm
     */
    public TaskCreationForm getTaskCreationForm() {
        return taskCreationForm;
    }

    /**
     * Setter for taskCreationForm.
     *
     * @param taskCreationForm the taskCreationForm to set
     */
    public void setTaskCreationForm(TaskCreationForm taskCreationForm) {
        this.taskCreationForm = taskCreationForm;
    }

}
