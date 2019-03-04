package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Add members to local group form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractMembersForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddToGroupForm extends AbstractMembersForm<Member> {

    /** Local groups. */
    private List<CollabProfile> localGroups;


    /**
     * Constructor.
     */
    public AddToGroupForm() {
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
     * Getter for localGroups.
     * 
     * @return the localGroups
     */
    public List<CollabProfile> getLocalGroups() {
        return localGroups;
    }

    /**
     * Setter for localGroups.
     * 
     * @param localGroups the localGroups to set
     */
    public void setLocalGroups(List<CollabProfile> localGroups) {
        this.localGroups = localGroups;
    }

}
