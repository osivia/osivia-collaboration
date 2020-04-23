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
     * Internationalization key.
     */
    private final String key;


    /**
     * Constructor.
     */
    ImageSourceType() {
        this.key = "EDITOR_IMAGE_SOURCE_TYPE_" + StringUtils.upperCase(this.name());
    }


    public String getKey() {
        return key;
    }
}
