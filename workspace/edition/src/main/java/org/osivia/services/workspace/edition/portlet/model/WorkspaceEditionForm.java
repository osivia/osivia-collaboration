package org.osivia.services.workspace.edition.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class WorkspaceEditionForm {

    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace type. */
    private WorkspaceType type;
    /** Workspace vignette. */
    private Vignette vignette;
    /** Workspace tasks. */
    private List<Task> tasks;
    /** Workspace action. */
    private String action;
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
     * Getter for vignette.
     * 
     * @return the vignette
     */
    public Vignette getVignette() {
        return vignette;
    }

    /**
     * Setter for vignette.
     * 
     * @param vignette the vignette to set
     */
    public void setVignette(Vignette vignette) {
        this.vignette = vignette;
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
     * Getter for action.
     * 
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * Setter for action.
     * 
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
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
