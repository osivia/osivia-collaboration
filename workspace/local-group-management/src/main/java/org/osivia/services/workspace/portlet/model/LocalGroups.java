package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local groups java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class LocalGroups {

    /** Workspace identifier. */
    private String workspaceId;
    /** Local groups. */
    private List<LocalGroup> groups;


    /**
     * Constructor.
     */
    public LocalGroups() {
        super();
    }


    /**
     * Getter for workspaceId.
     * 
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Setter for workspaceId.
     * 
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<LocalGroup> getGroups() {
        return this.groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<LocalGroup> groups) {
        this.groups = groups;
    }

}
