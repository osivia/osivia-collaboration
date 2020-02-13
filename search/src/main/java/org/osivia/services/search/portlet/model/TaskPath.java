package org.osivia.services.search.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task path java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskPath {

    /** Task CMS path. */
    private String cmsPath;
    /** Updated task indicator. */
    private boolean updated;


    /**
     * Constructor.
     */
    public TaskPath() {
        super();
    }


    /**
     * Getter for cmsPath.
     * 
     * @return the cmsPath
     */
    public String getCmsPath() {
        return cmsPath;
    }

    /**
     * Setter for cmsPath.
     * 
     * @param cmsPath the cmsPath to set
     */
    public void setCmsPath(String cmsPath) {
        this.cmsPath = cmsPath;
    }

    /**
     * Getter for updated.
     * 
     * @return the updated
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Setter for updated.
     * 
     * @param updated the updated to set
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

}
