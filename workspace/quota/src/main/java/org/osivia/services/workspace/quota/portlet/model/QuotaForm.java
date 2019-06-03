package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Quota form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class QuotaForm {

    /** Trashed documents. */
    private List<QuotaItem> quotaItems;
    /** Sort property. */

    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public QuotaForm() {
        super();
    }


    /**
     * Getter for trashedDocuments.
     * 
     * @return the trashedDocuments
     */
    public List<QuotaItem> getQuotaItems() {
        return quotaItems;
    }

    /**
     * Setter for trashedDocuments.
     * 
     * @param trashedDocuments the trashedDocuments to set
     */
    public void setQuotasItems(List<QuotaItem> quotaItems) {
        this.quotaItems = quotaItems;
    }



    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

}
