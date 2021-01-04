package org.osivia.services.editor.image.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Editor document image source form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageSourceDocumentForm {

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
    public EditorImageSourceDocumentForm() {
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
