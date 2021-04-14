package org.osivia.services.editor.common.model;

import java.util.List;

/**
 * Source document form abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class SourceDocumentForm {

    /**
     * Filter.
     */
    private String filter;
    /**
     * Search scope.
     */
    private SearchScope scope;
    /**
     * Available search scopes.
     */
    private List<SearchScope> availableScopes;


    /**
     * Constructor.
     */
    public SourceDocumentForm() {
        super();
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public SearchScope getScope() {
        return scope;
    }

    public void setScope(SearchScope scope) {
        this.scope = scope;
    }

    public List<SearchScope> getAvailableScopes() {
        return availableScopes;
    }

    public void setAvailableScopes(List<SearchScope> availableScopes) {
        this.availableScopes = availableScopes;
    }
}
