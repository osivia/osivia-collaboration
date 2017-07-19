package org.osivia.services.editor.link.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * URL types enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum UrlType {

    /** Manual URL type. */
    MANUAL,
    /** Document URL type. */
    DOCUMENT;


    /** Default URL type. */
    public static final UrlType DEFAULT = MANUAL;


    /** Identifier. */
    private final String id;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     */
    private UrlType() {
        this.id = this.name();
        this.key = "URL_TYPE_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get URL type from identifier.
     *
     * @param id URL type identifier
     * @return URL type
     */
    public static UrlType fromId(String id) {
        UrlType result = DEFAULT;
        for (UrlType type : UrlType.values()) {
            if (type.id.equals(id)) {
                result = type;
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
