package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local group edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroup
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class LocalGroupEditionForm extends LocalGroup {

    /** Workspace identifier. */
    private String workspaceId;
    /** Local group members. */
    private List<Member> members;
    /** Other workspace members. */
    private List<Member> otherMembers;
    /** Added member. */
    private Member addedMember;


    /**
     * Constructor.
     */
    public LocalGroupEditionForm() {
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
     * Getter for otherMembers.
     * 
     * @return the otherMembers
     */
    public List<Member> getOtherMembers() {
        return otherMembers;
    }

    /**
     * Setter for otherMembers.
     * 
     * @param otherMembers the otherMembers to set
     */
    public void setOtherMembers(List<Member> otherMembers) {
        this.otherMembers = otherMembers;
    }

    /**
     * Getter for addedMember.
     * 
     * @return the addedMember
     */
    public Member getAddedMember() {
        return addedMember;
    }

    /**
     * Setter for addedMember.
     * 
     * @param addedMember the addedMember to set
     */
    public void setAddedMember(Member addedMember) {
        this.addedMember = addedMember;
    }

}
