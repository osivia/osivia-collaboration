package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Local groups summary java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class LocalGroupsSummary {

    /** Local groups. */
    private List<LocalGroupsSummaryItem> groups;
    /** Sort property. */
    private LocalGroupsSort sort;
    /** Alternative sort indicator. */
    private boolean alt;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor
     */
    public LocalGroupsSummary() {
        super();
    }


    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<LocalGroupsSummaryItem> getGroups() {
        return groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<LocalGroupsSummaryItem> groups) {
        this.groups = groups;
    }

    /**
     * Getter for sort.
     * 
     * @return the sort
     */
    public LocalGroupsSort getSort() {
        return sort;
    }

    /**
     * Setter for sort.
     * 
     * @param sort the sort to set
     */
    public void setSort(LocalGroupsSort sort) {
        this.sort = sort;
    }

    /**
     * Getter for alt.
     * 
     * @return the alt
     */
    public boolean isAlt() {
        return alt;
    }

    /**
     * Setter for alt.
     * 
     * @param alt the alt to set
     */
    public void setAlt(boolean alt) {
        this.alt = alt;
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
