package org.osivia.services.statistics.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Visits views enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum VisitsView {

    /** Days. */
    DAYS,
    /** Months. */
    MONTHS;


    /** Default value. */
    public static final VisitsView DEFAULT = DAYS;


    /** Internationalization label key prefix. */
    private static final String KEY_PREFIX = "VISITS_VIEW_";


    /** Internationalization label key. */
    private final String key;


    /**
     * Constructor.
     */
    private VisitsView() {
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
