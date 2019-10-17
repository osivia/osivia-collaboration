package org.osivia.services.workspace.filebrowser.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * File browser item sort enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum FileBrowserSortEnum implements FileBrowserSortField {

    /**
     * Relevance sort.
     */
    RELEVANCE("relevance", true),
    /**
     * Title sort.
     */
    TITLE("title"),
    /**
     * Location sort.
     */
    LOCATION("location", true),
    /**
     * Last modification sort.
     */
    LAST_MODIFICATION("last-modification"),
    /**
     * File size sort.
     */
    FILE_SIZE("file-size");
    

    /**
     * Identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;
    /**
     * List mode restriction indicator.
     */
    private final boolean listMode;


    /**
     * Constructor.
     *
     * @param id       identifier
     * @param listMode list mode restriction indicator
     */
    private FileBrowserSortEnum(String id, boolean listMode) {
        this.id = id;
        this.key = "FILE_BROWSER_SORT_FIELD_" + StringUtils.upperCase(this.name());
        this.listMode = listMode;
    }


    /**
     * Constructor.
     *
     * @param id identifier
     */
    private FileBrowserSortEnum(String id) {
        this(id, false);
    }


    @Override
    public String getId() {
        return this.id;
    }


    @Override
    public String getKey() {
        return this.key;
    }


    @Override
    public boolean isListMode() {
        return this.listMode;
    }

}
