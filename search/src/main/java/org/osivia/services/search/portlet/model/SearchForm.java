package org.osivia.services.search.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchForm {

    /** Query. */
    private String query;


    /**
     * Constructor.
     */
    public SearchForm() {
        super();
    }


    /**
     * Getter for query.
     * 
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Setter for query.
     * 
     * @param query the query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

}
