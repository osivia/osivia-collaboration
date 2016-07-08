package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Member management options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberManagementOptions {

    /** Workspace identifier. */
    private String workspaceId;
    /** Invitations count. */
    private int invitationsCount;
    /** Requests count. */
    private int requestsCount;
    /** Roles. */
    private List<WorkspaceRole> roles;


    /**
     * Constructor.
     */
    public MemberManagementOptions() {
        super();
    }

    
    /**
     * Getter for workspaceId.
     * @return the workspaceId
     */
    public String getWorkspaceId() {
        return workspaceId;
    }

    
    /**
     * Setter for workspaceId.
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Getter for invitationsCount.
     * @return the invitationsCount
     */
    public int getInvitationsCount() {
        return invitationsCount;
    }
    
    /**
     * Setter for invitationsCount.
     * @param invitationsCount the invitationsCount to set
     */
    public void setInvitationsCount(int invitationsCount) {
        this.invitationsCount = invitationsCount;
    }

    /**
     * Getter for requestsCount.
     * @return the requestsCount
     */
    public int getRequestsCount() {
        return requestsCount;
    }
    
    /**
     * Setter for requestsCount.
     * @param requestsCount the requestsCount to set
     */
    public void setRequestsCount(int requestsCount) {
        this.requestsCount = requestsCount;
    }

    /**
     * Getter for roles.
     * 
     * @return the roles
     */
    public List<WorkspaceRole> getRoles() {
        return roles;
    }

    /**
     * Setter for roles.
     * 
     * @param roles the roles to set
     */
    public void setRoles(List<WorkspaceRole> roles) {
        this.roles = roles;
    }

}
