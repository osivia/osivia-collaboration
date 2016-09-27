package org.osivia.services.workspace.edition.portlet.model;

import org.osivia.portal.api.taskbar.TaskbarItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task java-bean.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarItemDecorator
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Task extends TaskbarItemDecorator {

    /** Path. */
    private String path;
    /** Display name. */
    private String displayName;
    /** Description. */
    private String description;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Task [displayName=");
        builder.append(this.displayName);
        builder.append(", active=");
        builder.append(this.active);
        builder.append(", order=");
        builder.append(this.order);
        builder.append("]");
        return builder.toString();
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
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
