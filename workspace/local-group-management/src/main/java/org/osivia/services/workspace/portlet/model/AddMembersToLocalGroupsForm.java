package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Add members to local groups form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddMembersToLocalGroupsForm {

    /** Local groups. */
    private List<AbstractLocalGroup> groups;
    /** Members. */
    private List<LocalGroupMember> members;


    /**
     * Constructor.
     */
    public AddMembersToLocalGroupsForm() {
        super();
    }


    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<AbstractLocalGroup> getGroups() {
        return groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<AbstractLocalGroup> groups) {
        this.groups = groups;
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
