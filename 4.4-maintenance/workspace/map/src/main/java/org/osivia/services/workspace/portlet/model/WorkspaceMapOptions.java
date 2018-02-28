package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Workspace map options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceMapOptions {

    /** Workspace path. */
    private String workspacePath;
    /** Navigation path. */
    private String navigationPath;


    /**
     * Constructor.
     */
    public WorkspaceMapOptions() {
        super();
    }


    /**
     * Getter for workspacePath.
     * 
     * @return the workspacePath
     */
    public String getWorkspacePath() {
        return workspacePath;
    }

    /**
     * Setter for workspacePath.
     * 
     * @param workspacePath the workspacePath to set
     */
    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    /**
     * Getter for navigationPath.
     * 
     * @return the navigationPath
     */
    public String getNavigationPath() {
        return navigationPath;
    }

    /**
     * Setter for navigationPath.
     * 
     * @param navigationPath the navigationPath to set
     */
    public void setNavigationPath(String navigationPath) {
        this.navigationPath = navigationPath;
    }

}
