package org.osivia.services.workspace.edition.portlet.model;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.portlet.Refreshable;
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
    /** Workspace root type indicator. */
    private boolean root;
    /** Workspace template (only for root). */
    private String template;
    /** Workspace templates (only for root). */
    private Map<String, String> templates;
    /** Workspace type (only for root). */
    private WorkspaceType workspaceType;
    /** Initial workspace type (only for root). */
    private WorkspaceType initialWorkspaceType;
    /** Workspace visual. */
    private Image visual;

    /** Workspace tasks. */
    private List<Task> tasks;
    /** Workspace editorial. */
    private Editorial editorial;

    /** Workspace Nuxeo document. */
    private final Document document;
    /** Portal administrator indicator. */
    private final boolean admin;
    /** Workspace types. */
    private final List<WorkspaceType> workspaceTypes;


    /**
     * Constructor.
     * 
     * @param document workspace Nuxeo document
     * @param admin portal administrator indicator
     */
    public WorkspaceEditionForm(Document document, boolean admin) {
        super();
        this.document = document;
        this.admin = admin;
        this.workspaceTypes = WorkspaceType.list(admin);
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
     * Getter for root.
     * 
     * @return the root
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * Setter for root.
     * 
     * @param root the root to set
     */
    public void setRoot(boolean root) {
        this.root = root;
    }

    /**
     * Getter for template.
     * 
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Setter for template.
     * 
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Getter for templates.
     * 
     * @return the templates
     */
    public Map<String, String> getTemplates() {
        return templates;
    }

    /**
     * Setter for templates.
     * 
     * @param templates the templates to set
     */
    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    /**
     * Getter for workspaceType.
     * 
     * @return the workspaceType
     */
    public WorkspaceType getWorkspaceType() {
        return workspaceType;
    }

    /**
     * Setter for workspaceType.
     * 
     * @param workspaceType the workspaceType to set
     */
    public void setWorkspaceType(WorkspaceType workspaceType) {
        this.workspaceType = workspaceType;
    }

    /**
     * Getter for initialWorkspaceType.
     * 
     * @return the initialWorkspaceType
     */
    public WorkspaceType getInitialWorkspaceType() {
        return initialWorkspaceType;
    }

    /**
     * Setter for initialWorkspaceType.
     * 
     * @param initialWorkspaceType the initialWorkspaceType to set
     */
    public void setInitialWorkspaceType(WorkspaceType initialWorkspaceType) {
        this.initialWorkspaceType = initialWorkspaceType;
    }

    /**
     * Getter for visual.
     * 
     * @return the visual
     */
    public Image getVisual() {
        return visual;
    }

    /**
     * Setter for visual.
     * 
     * @param visual the visual to set
     */
    public void setVisual(Image visual) {
        this.visual = visual;
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
     * Getter for editorial.
     * 
     * @return the editorial
     */
    public Editorial getEditorial() {
        return editorial;
    }

    /**
     * Setter for editorial.
     * 
     * @param editorial the editorial to set
     */
    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
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
     * Getter for admin.
     * 
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Getter for workspaceTypes.
     * 
     * @return the workspaceTypes
     */
    public List<WorkspaceType> getWorkspaceTypes() {
        return workspaceTypes;
    }

}
