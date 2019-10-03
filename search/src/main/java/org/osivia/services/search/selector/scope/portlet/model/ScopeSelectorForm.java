package org.osivia.services.search.selector.scope.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Scope selector form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScopeSelectorForm {

    /** Label. */
    private String label;
    /** Scope. */
    private SearchScope scope;

    /** Scopes. */
    private List<SearchScope> scopes;


    /**
     * Constructor.
     */
    public ScopeSelectorForm() {
        super();
    }


    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for scope.
     * @return the scope
     */
    public SearchScope getScope() {
        return scope;
    }

    /**
     * Setter for scope.
     * @param scope the scope to set
     */
    public void setScope(SearchScope scope) {
        this.scope = scope;
    }

    /**
     * Getter for scopes.
     * @return the scopes
     */
    public List<SearchScope> getScopes() {
        return scopes;
    }

    /**
     * Setter for scopes.
     * @param scopes the scopes to set
     */
    public void setScopes(List<SearchScope> scopes) {
        this.scopes = scopes;
    }

}
