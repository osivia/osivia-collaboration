package org.osivia.services.search.selector.type.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Search types enumeration.
 * 
 * @author Loïc Billon
 */
public enum SearchType {

    /** All. */
    ALL("*"),
    /** Workspaces. */
    WORKSPACE("Workspace"),
    /** Workspaces. */
    FILE("File"),
    /** Workspaces. */
    USERPROFILE("UserProfile");



    /** Internationalization key prefix. */
    private static final String KEY_PREFIX = "TYPE_";


    /** Internationalization key. */
    private final String key;

    private final String doctype;

    /**
     * Constructor.
     */
    private SearchType(String doctype) {
        this.key = KEY_PREFIX + StringUtils.upperCase(this.name());
        this.doctype = doctype;
        
    }


    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }
    
    public String getDocType() {
    	return doctype;
    }

}
