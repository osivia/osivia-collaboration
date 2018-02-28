package org.osivia.services.workspace.portlet.model;

import java.util.Arrays;
import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace creation form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class WorkspaceCreationForm {

    /** Workspace title. */
    private String title;
    /** Workspace description. */
    private String description;
    /** Workspace type. */
    private WorkspaceType type;
    /** Workspace owner identifier. */
    private String owner;

    /** Workspace types. */
    private final List<WorkspaceType> types;


    /**
     * Constructor.
     */
    public WorkspaceCreationForm() {
        super();
        this.types = Arrays.asList(WorkspaceType.values());
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
     * Getter for owner.
     * 
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Setter for owner.
     * 
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Getter for types.
     * 
     * @return the types
     */
    public List<WorkspaceType> getTypes() {
        return types;
    }

}
