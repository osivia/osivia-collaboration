package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Local groups sorts enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum LocalGroupsSort {

    /** Display name. */
    DISPLAY_NAME("display-name"),
    /** Members count. */
    MEMBERS_COUNT("members-count");


    /** Identifier. */
    private final String id;


    /**
     * Constructor.
     * 
     * @param id identifier
     */
    private LocalGroupsSort(String id) {
        this.id = id;
    }


    /**
     * Get sort from identifier.
     * 
     * @param id identifier
     * @return sort
     */
    public static LocalGroupsSort fromId(String id) {
        LocalGroupsSort result = DISPLAY_NAME;

        for (LocalGroupsSort value : LocalGroupsSort.values()) {
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
