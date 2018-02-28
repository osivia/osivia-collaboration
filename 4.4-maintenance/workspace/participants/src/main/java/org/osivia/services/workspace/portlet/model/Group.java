package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Workspace participant group java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Group {

    /** Workspace role. */
    private WorkspaceRole role;
    /** Group members. */
    private List<Member> members;
    /** More members count. */
    private int more;


	/**
     * Constructor.
     */
    public Group() {
        super();
    }


    /**
     * Getter for role.
     * 
     * @return the role
     */
    public WorkspaceRole getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    /**
     * Getter for members.
     * 
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Setter for members.
     * 
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    /**
     * Getter for more.
     * 
     * @return the more
     */
    public int getMore() {
        return more;
    }

    /**
     * Setter for more.
     * 
     * @param more the more to set
     */
    public void setMore(int more) {
        this.more = more;
    }

}
