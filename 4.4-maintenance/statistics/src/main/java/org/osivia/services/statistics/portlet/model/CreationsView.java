package org.osivia.services.statistics.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Creation view enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum CreationsView {

    /** Differential view. */
    DIFFERENTIAL,
    /** Aggregate view. */
    AGGREGATE;


    /** Default view. */
    public static final CreationsView DEFAULT = DIFFERENTIAL;
    

    /** Internationalization label key prefix. */
    private static final String KEY_PREFIX = "CREATIONS_VIEW_";


    /** Internationalization label key. */
    private final String key;


    /**
     * Constructor.
     */
    private CreationsView() {
        this.key = KEY_PREFIX + StringUtils.upperCase(this.name());
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
