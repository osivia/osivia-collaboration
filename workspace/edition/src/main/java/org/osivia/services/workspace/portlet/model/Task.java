package org.osivia.services.workspace.portlet.model;

import org.osivia.portal.api.taskbar.TaskbarItem;

/**
 * Task java-bean.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarItemDecorator
 */
public class Task extends TaskbarItemDecorator {

    /** Path. */
    private String path;
    /** Display name. */
    private String displayName;
    /** Active indicator. */
    private boolean active;
    /** Order. */
    private int order;


    /**
     * Constructor.
     *
     * @param item taskbar item
     */
    public Task(TaskbarItem item) {
        super(item);
    }


    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setter for path.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
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

    /**
     * Getter for order.
     *
     * @return the order
     */
    @Override
    public int getOrder() {
        return this.order;
    }

    /**
     * Setter for order.
     *
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

}
