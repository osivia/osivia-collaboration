package org.osivia.services.workspace.portlet.model;

import org.osivia.portal.api.taskbar.TaskbarTask;

/**
 * Task java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Task {

    /** Display name. */
    private String displayName;
    /** Active indicator. */
    private boolean active;
    /** Order. */
    private int order;

    /** Identifier. */
    private final String id;
    /** Icon. */
    private final String icon;
    /** Path. */
    private final String path;


    /**
     * Constructor.
     *
     * @param task taskbar task
     */
    public Task(TaskbarTask task) {
        super();
        this.id = task.getId();
        this.icon = task.getIcon();
        this.path = task.getPath();
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

    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return this.icon;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

}
