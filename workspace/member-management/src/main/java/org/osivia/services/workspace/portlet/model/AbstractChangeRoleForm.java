package org.osivia.services.workspace.portlet.model;

import org.osivia.directory.v2.model.ext.WorkspaceRole;

/**
 * Change role abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @param <E> parameterized type
 * @see AbstractMembersForm
 */
public class AbstractChangeRoleForm<E extends MemberObject> extends AbstractMembersForm<E> {

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
