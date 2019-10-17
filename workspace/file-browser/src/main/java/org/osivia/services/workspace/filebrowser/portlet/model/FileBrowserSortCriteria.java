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

    /**
     * Sort field.
     */
    private FileBrowserSortField field;
    /**
     * Alternative sort indicator.
     */
    private boolean alt;


    /**
     * Constructor.
     */
    public FileBrowserSortCriteria() {
        super();
    }


    public FileBrowserSortField getField() {
        return field;
    }

    public void setField(FileBrowserSortField field) {
        this.field = field;
    }

    public boolean isAlt() {
        return alt;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }
}
