package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change member role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractChangeRoleForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeMemberRoleForm extends AbstractChangeRoleForm<Member> {

    /**
     * Constructor.
     */
    public ChangeMemberRoleForm() {
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

}
