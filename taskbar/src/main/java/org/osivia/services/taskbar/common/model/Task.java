package org.osivia.services.taskbar.common.model;

import javax.portlet.PortletException;

import org.apache.commons.beanutils.BeanUtils;
import org.osivia.portal.api.taskbar.TaskbarTask;

/**
 * Task java-bean.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarTask
 */
public class Task extends TaskbarTask {

    /** URL. */
    private String url;
    /** Active indicator. */
    private boolean active;


    /**
     * Constructor.
     */
    public Task() {
        super();
    }


    /**
     * Constructor.
     *
     * @param task taskbar task
     * @throws PortletException
     */
    public Task(TaskbarTask task) throws PortletException {
        this();

        try {
            BeanUtils.copyProperties(this, task);
        } catch (Exception e) {
            throw new PortletException(e);
        }
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
