package org.osivia.services.editor.common.model;

import org.apache.commons.lang.StringUtils;

/**
 * Documents search scopes enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum SearchScope {

    /**
     * Current workspace.
     */
    WORKSPACE,
    /**
     * Everywhere.
     */
    EVERYWHERE;


    /**
     * Default search scope.
     */
    public static final SearchScope DEFAULT = WORKSPACE;


    /**
     * Identifier.
     */
    private final String id;
    /**
     * Internationalization key.
     */
    private final String key;


    /**
     * Constructor.
     */
    SearchScope() {
        this.id = StringUtils.lowerCase(this.name());
        this.key = "EDITOR_SEARCH_SCOPE_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get search scope from identifier.
     *
     * @param id identifier
     * @return search scope
     */
    public static SearchScope fromId(String id) {
        SearchScope result = DEFAULT;
        for (SearchScope value : SearchScope.values()) {
            if (StringUtils.equalsIgnoreCase(id, value.id)) {
                result = value;
            }
        }
        return result;
    }


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
