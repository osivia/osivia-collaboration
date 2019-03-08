package org.osivia.services.workspace.portlet.model;

import org.osivia.directory.v2.model.ext.WorkspaceRole;

/**
 * Change role form abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @param <M> member type
 * @see AbstractMembersForm
 */
public abstract class AbstractChangeRoleForm<M extends MemberObject> extends AbstractMembersForm<M> {

    /** Role. */
    private WorkspaceRole role;


    /**
     * Constructor.
     */
    public AbstractChangeRoleForm() {
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

}
