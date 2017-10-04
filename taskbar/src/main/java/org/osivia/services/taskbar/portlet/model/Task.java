package org.osivia.services.taskbar.portlet.model;

import org.osivia.portal.api.taskbar.TaskbarTask;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task java-bean.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarTaskDecorator
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task extends TaskbarTaskDecorator {

    /** Display name. */
    private String displayName;
    /** URL. */
    private String url;
    /** Active indicator. */
    private boolean active;


    /**
     * Constructor.
     */
    public Task(TaskbarTask task) {
        super(task);
    }


    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for url.
     *
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for active.
     *
     * @return the active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Setter for active.
     *
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
