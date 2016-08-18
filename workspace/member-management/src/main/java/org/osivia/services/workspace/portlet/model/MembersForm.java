package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Members form java-bean.
 * TODO LBI manager lifecycle of this object
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(value="session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MembersForm {

    /** Members. */
    private List<Member> members;


    /**
     * Constructor.
     */
    public MembersForm() {
        super();
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

}
