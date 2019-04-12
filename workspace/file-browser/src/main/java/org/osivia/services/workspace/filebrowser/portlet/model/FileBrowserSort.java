package org.osivia.services.workspace.filebrowser.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * File browser item sort enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum FileBrowserSort {

    /** Title sort. */
    TITLE("title"),
    /** Last modification sort. */
    LAST_MODIFICATION("last-modification"),
    /** File size sort. */
    FILE_SIZE("file-size");


    /** Identifier. */
    private final String id;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     * 
     * @param id identifier
     */
    private FileBrowserSort(String id) {
        this.id = id;
        this.key = "FILE_BROWSER_SORT_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get criteria from identifier.
     * 
     * @param id identifier
     * @return criteria
     */
    public static FileBrowserSort fromId(String id) {
        FileBrowserSort result = TITLE;

        for (FileBrowserSort value : FileBrowserSort.values()) {
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

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
