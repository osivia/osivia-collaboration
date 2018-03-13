package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Member management options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Refreshable
public class MemberManagementOptions {

    /** Workspace identifier. */
    private String workspaceId;
    /** Workspace type. */
    private WorkspaceType workspaceType;
    /** Invitations count. */
    private int invitationsCount;
    /** Requests count. */
    private int requestsCount;
    /** Roles. */
    private List<WorkspaceRole> roles;
    /** Workspace local groups. */
    private List<CollabProfile> workspaceLocalGroups;


    /**
     * Constructor.
     */
    public MemberManagementOptions() {
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
     * Getter for invitationsCount.
     * 
     * @return the invitationsCount
     */
    public int getInvitationsCount() {
        return invitationsCount;
    }

    /**
     * Setter for invitationsCount.
     * 
     * @param invitationsCount the invitationsCount to set
     */
    public void setInvitationsCount(int invitationsCount) {
        this.invitationsCount = invitationsCount;
    }

    /**
     * Getter for requestsCount.
     * 
     * @return the requestsCount
     */
    public int getRequestsCount() {
        return requestsCount;
    }

    /**
     * Setter for requestsCount.
     * 
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

    /**
     * Getter for workspaceLocalGroups.
     * 
     * @return the workspaceLocalGroups
     */
    public List<CollabProfile> getWorkspaceLocalGroups() {
        return workspaceLocalGroups;
    }

    /**
     * Setter for workspaceLocalGroups.
     * 
     * @param workspaceLocalGroups the workspaceLocalGroups to set
     */
    public void setWorkspaceLocalGroups(List<CollabProfile> workspaceLocalGroups) {
        this.workspaceLocalGroups = workspaceLocalGroups;
    }

}
