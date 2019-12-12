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
    /** Workspace welcome title (only for root). */
    private String welcomeTitle;
    /** Workspace description. */
    private String description;
    /** Workspace template (only for root). */
    private String template;
    /** Workspace templates (only for root). */
    private Map<String, String> templates;
    /** Workspace type (only for root). */
    private WorkspaceType workspaceType;
    /** Allowed invitation requests indicator (only for root). */
    private boolean allowedInvitationRequests;
    /** Allowed comments on documents. */
    private boolean spaceCommentable;
    /** Workspace vignette. */
    private Image vignette;
    /** Workspace tasks. */
    private List<Task> tasks;
    /** Workspace editorial. */
    private Editorial editorial;
    /** Workspace other tasks. */
    private List<Task> otherTasks;

    /** Initial workspace type (only for root). */
    private WorkspaceType initialWorkspaceType;
    /** Workspace types. */
    private List<WorkspaceType> workspaceTypes;


    /** Workspace Nuxeo document. */
    private final Document document;
    /** Workspace root type indicator. */
    private final boolean root;


    /**
     * Constructor.
     * 
     * @param document workspace Nuxeo document
     * @param root workspace root type indicator
     */
    public WorkspaceEditionForm(Document document, boolean root) {
        super();
        this.document = document;
        this.root = root;
    }


    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
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
     * Getter for welcomeTitle.
     * 
     * @return the welcomeTitle
     */
    public String getWelcomeTitle() {
        return welcomeTitle;
    }

    /**
     * Setter for welcomeTitle.
     * 
     * @param welcomeTitle the welcomeTitle to set
     */
    public void setWelcomeTitle(String welcomeTitle) {
        this.welcomeTitle = welcomeTitle;
    }

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
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
     * Getter for allowedInvitationRequests.
     * 
     * @return the allowedInvitationRequests
     */
    public boolean isAllowedInvitationRequests() {
        return allowedInvitationRequests;
    }

    /**
     * Setter for allowedInvitationRequests.
     * 
     * @param allowedInvitationRequests the allowedInvitationRequests to set
     */
    public void setAllowedInvitationRequests(boolean allowedInvitationRequests) {
        this.allowedInvitationRequests = allowedInvitationRequests;
    }

    
    /**
     * Getter for spaceCommentable.
     * @return
     */
    public boolean isSpaceCommentable() {
		return spaceCommentable;
	}

    /**
     * Setter for spaceCommentable.
     * @param spaceCommentable
     */
	public void setSpaceCommentable(boolean spaceCommentable) {
		this.spaceCommentable = spaceCommentable;
	}


	/**
     * Getter for vignette.
     * 
     * @return the vignette
     */
    public Image getVignette() {
        return vignette;
    }

    /**
     * Setter for vignette.
     * 
     * @param vignette the vignette to set
     */
    public void setVignette(Image vignette) {
        this.vignette = vignette;
    }

    /**
     * Getter for tasks.
     * 
     * @return the tasks
     */
    public List<Task> getTasks() {
        return tasks;
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
     * Getter for otherTasks.
     * 
     * @return the otherTasks
     */
    public List<Task> getOtherTasks() {
        return otherTasks;
    }

    /**
     * Setter for otherTasks.
     * 
     * @param otherTasks the otherTasks to set
     */
    public void setOtherTasks(List<Task> otherTasks) {
        this.otherTasks = otherTasks;
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
     * Getter for workspaceTypes.
     * 
     * @return the workspaceTypes
     */
    public List<WorkspaceType> getWorkspaceTypes() {
        return workspaceTypes;
    }

    /**
     * Setter for workspaceTypes.
     * 
     * @param workspaceTypes the workspaceTypes to set
     */
    public void setWorkspaceTypes(List<WorkspaceType> workspaceTypes) {
        this.workspaceTypes = workspaceTypes;
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
     * Getter for root.
     * 
     * @return the root
     */
    public boolean isRoot() {
        return root;
    }

}
