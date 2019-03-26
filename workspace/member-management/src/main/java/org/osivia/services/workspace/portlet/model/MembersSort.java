package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Members sort enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum MembersSort {

    /** Member display name. */
    MEMBER_DISPLAY_NAME("display-name"),
    /** Acknowledgment or invitation date. */
    DATE("date"),
    /** Role. */
    ROLE("role"),
    /** Invitation state. */
    INVITATION_STATE("state");


    /** Default. */
    public static final MembersSort DEFAULT = DATE;


    /** Identifier. */
    private final String id;


    /**
     * Constructor.
     * 
     * @param id identifier
     */
    private MembersSort(String id) {
        this.id = id;
    }


    /**
     * Get sort from identifier.
     * 
     * @param id identifier
     * @return sort
     */
    public static MembersSort fromId(String id) {
        MembersSort result = DEFAULT;

        for (MembersSort value : MembersSort.values()) {
            if (StringUtils.equalsIgnoreCase(id, value.id)) {
                result = value;
                break;
            }
        }

        return result;
    }


    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

}
