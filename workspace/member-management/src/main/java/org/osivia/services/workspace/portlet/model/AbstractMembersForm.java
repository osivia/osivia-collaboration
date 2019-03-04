package org.osivia.services.workspace.portlet.model;

import java.util.List;

/**
 * Members form java-bean abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class AbstractMembersForm {

    /** Members. */
    private List<Member> members;
    /** Sort. */
    private MembersSort sort;
    /** Alternative sort indicator. */
    private boolean alt;


    /**
     * Constructor.
     */
    public AbstractMembersForm() {
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

    /**
     * Getter for sort.
     * 
     * @return the sort
     */
    public MembersSort getSort() {
        return sort;
    }

    /**
     * Setter for sort.
     * 
     * @param sort the sort to set
     */
    public void setSort(MembersSort sort) {
        this.sort = sort;
    }

    /**
     * Getter for alt.
     * 
     * @return the alt
     */
    public boolean isAlt() {
        return alt;
    }

    /**
     * Setter for alt.
     * 
     * @param alt the alt to set
     */
    public void setAlt(boolean alt) {
        this.alt = alt;
    }

}
