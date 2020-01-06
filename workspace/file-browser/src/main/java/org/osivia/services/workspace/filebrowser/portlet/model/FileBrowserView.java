package org.osivia.services.workspace.filebrowser.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * File browser views enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum FileBrowserView {

    /** Table view. */
    TABLE("glyphicons glyphicons-basic-thumbnails-list"),
    /** Thumbnails view. */
    THUMBNAILS("glyphicons glyphicons-basic-thumbnails");

    
    /** Default view. */
    public static final FileBrowserView DEFAULT = TABLE;
    

    /** View identifier. */
    private final String id;
    /** View internationalization key. */
    private final String key;
    /** View icon. */
    private final String icon;


    /**
     * Constructor.
     * 
     * @param icon view icon
     */
    private FileBrowserView(String icon) {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "FILE_BROWSER_VIEW_" + StringUtils.upperCase(this.name());
        this.icon = icon;
    }

    
    /**
     * Get view from identifier.
     * 
     * @param id identifier
     * @return view
     */
    public static FileBrowserView fromId(String id) {
        FileBrowserView result = DEFAULT;

        for (FileBrowserView value : FileBrowserView.values()) {
            if (StringUtils.equals(id, value.id)) {
                result = value;
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

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }
    
}
