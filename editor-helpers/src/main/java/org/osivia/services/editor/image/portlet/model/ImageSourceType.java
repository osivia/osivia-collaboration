package org.osivia.services.editor.image.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Image source types enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum ImageSourceType {

    /**
     * Attached image.
     */
    ATTACHED("glyphicons glyphicons-paperclip"),
    /**
     * Document.
     */
    DOCUMENT("glyphicons glyphicons-search");


    /**
     * identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;
    /**
     * Icon.
     */
    private final String icon;


    /**
     * Constructor.
     */
    ImageSourceType(String icon) {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "EDITOR_IMAGE_SOURCE_TYPE_" + StringUtils.upperCase(this.name());
        this.icon = icon;
    }


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getIcon() {
        return icon;
    }
}
