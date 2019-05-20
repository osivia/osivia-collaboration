package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

public enum TrashFormSort {

    /**
     * Document sort.
     */
    DOCUMENT("document"),
    /**
     * Date sort.
     */
    DATE("date"),
    /**
     * Location.
     */
    LOCATION("location");


    /**
     * Identifier.
     */
    private final String id;


    /**
     * Constructor.
     *
     * @param id identifier
     */
    TrashFormSort(String id) {
        this.id = id;
    }


    /**
     * Get sort from identifier.
     *
     * @param id identifier
     * @return sort
     */
    public static TrashFormSort fromId(String id) {
        TrashFormSort result = DOCUMENT;

        for (TrashFormSort value : TrashFormSort.values()) {
            if (StringUtils.equalsIgnoreCase(id, value.id)) {
                result = value;
                break;
            }
        }

        return result;
    }


    public String getId() {
        return id;
    }

}
