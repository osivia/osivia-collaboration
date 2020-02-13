package org.osivia.services.search.selector.scope.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Search scopes enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum SearchScope {

    /** Global. */
    GLOBAL,
    /** Local. */
    LOCAL;


    /** Internationalization key prefix. */
    private static final String KEY_PREFIX = "SCOPE_";


    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     */
    private SearchScope() {
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
