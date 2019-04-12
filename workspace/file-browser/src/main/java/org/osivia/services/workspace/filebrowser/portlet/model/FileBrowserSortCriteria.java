package org.osivia.services.workspace.filebrowser.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser sort criteria java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserSortCriteria {

    /** Sort. */
    private FileBrowserSort sort;
    /** Alternative sort indicator. */
    private boolean alt;


    /**
     * Constructor.
     */
    public FileBrowserSortCriteria() {
        super();
    }


    /**
     * Getter for sort.
     * 
     * @return the sort
     */
    public FileBrowserSort getSort() {
        return sort;
    }

    /**
     * Setter for sort.
     * 
     * @param sort the sort to set
     */
    public void setSort(FileBrowserSort sort) {
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

}
