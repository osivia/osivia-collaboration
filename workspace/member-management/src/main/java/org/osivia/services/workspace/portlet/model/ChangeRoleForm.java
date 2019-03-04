package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMembersForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeRoleForm extends AbstractMembersForm<Member> {

    /** Role. */
    private WorkspaceRole role;


    /**
     * Constructor.
     */
    public ChangeRoleForm() {
        super();
    }


    /**
     * Getter for selectedMembers.
     * 
     * @return the selectedMembers
     */
    public List<Member> getSelectedMembers() {
        return this.getMembers();
    }

    /**
     * Setter for selectedMembers.
     * 
     * @param selectedMembers the selectedMembers to set
     */
    public void setSelectedMembers(List<Member> selectedMembers) {
        this.setMembers(selectedMembers);
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
