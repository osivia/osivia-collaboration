package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Trash form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class TrashForm {

    /** Trashed documents. */
    private List<TrashedDocument> trashedDocuments;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public TrashForm() {
        super();
    }


    /**
     * Getter for trashedDocuments.
     * 
     * @return the trashedDocuments
     */
    public List<TrashedDocument> getTrashedDocuments() {
        return trashedDocuments;
    }

    /**
     * Setter for trashedDocuments.
     * 
     * @param trashedDocuments the trashedDocuments to set
     */
    public void setTrashedDocuments(List<TrashedDocument> trashedDocuments) {
        this.trashedDocuments = trashedDocuments;
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
