package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local group form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractLocalGroup
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupForm extends AbstractLocalGroup {

    /** Members. */
    private List<LocalGroupMember> members;


    /**
     * Constructor.
     */
    public LocalGroupForm() {
        super();
    }


    /**
     * Getter for members.
     * 
     * @return the members
     */
    public List<LocalGroupMember> getMembers() {
        return members;
    }

    /**
     * Setter for members.
     * 
     * @param members the members to set
     */
    public void setMembers(List<LocalGroupMember> members) {
        this.members = members;
    }

}
