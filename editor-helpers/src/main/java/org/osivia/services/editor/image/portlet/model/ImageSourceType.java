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
    ATTACHED,
    /**
     * Document.
     */
    DOCUMENT;


    /**
     * identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;


    /**
     * Constructor.
     */
    ImageSourceType() {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "EDITOR_IMAGE_SOURCE_TYPE_" + StringUtils.upperCase(this.name());
    }


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
