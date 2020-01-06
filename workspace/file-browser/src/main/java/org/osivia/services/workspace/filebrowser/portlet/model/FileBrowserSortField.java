package org.osivia.services.workspace.filebrowser.portlet.model;

/**
 * File browser sort field interface.
 */
public interface FileBrowserSortField {

    /**
     * Get field identifier.
     *
     * @return identifier
     */
    String getId();


    /**
     * Get field internationalization key.
     *
     * @return internationalization key
     */
    String getKey();


    /**
     * Get list mode restriction indicator.
     *
     * @return list mode restriction indicator
     */
    boolean isListMode();

}
