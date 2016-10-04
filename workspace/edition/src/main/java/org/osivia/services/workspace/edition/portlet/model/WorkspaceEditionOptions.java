package org.osivia.services.workspace.edition.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Workspace edition options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceEditionOptions {

    /** Workspace document. */
    private Document workspace;
    /** Workspace path. */
    private String path;
    /** Workspace type. */
    private String type;


    /**
     * Constructor.
     */
    public WorkspaceEditionOptions() {
        super();
    }


    /**
     * Getter for workspace.
     * 
     * @return the workspace
     */
    public Document getWorkspace() {
        return workspace;
    }

    /**
     * Setter for workspace.
     * 
     * @param workspace the workspace to set
     */
    public void setWorkspace(Document workspace) {
        this.workspace = workspace;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
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
