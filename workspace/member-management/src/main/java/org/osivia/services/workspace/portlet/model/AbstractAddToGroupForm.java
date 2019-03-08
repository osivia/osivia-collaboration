package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;

/**
 * Add members to local group form abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @param <M> member type
 * @see AbstractMembersForm
 */
public abstract class AbstractAddToGroupForm<M extends MemberObject> extends AbstractMembersForm<M> {

    /** Local groups. */
    private List<CollabProfile> localGroups;


    /**
     * Constructor.
     */
    public AbstractAddToGroupForm() {
        super();
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
