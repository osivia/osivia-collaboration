package org.osivia.services.workspace.edition.portlet.model;

import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarTask;
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
public class Task extends TaskbarItemDecorator implements TaskbarTask {

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
    /** Custom task indicator. */
    private boolean custom;
    /** Updated task indicator. */
    private boolean updated;
    /** Sorted task indicator. */
    private boolean sorted;


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
    public String getTitle() {
        return this.displayName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.path;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return !this.active;
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

    /**
     * Getter for custom.
     * 
     * @return the custom
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * Setter for custom.
     * 
     * @param custom the custom to set
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
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

    /**
     * Getter for sorted.
     * 
     * @return the sorted
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * Setter for sorted.
     * 
     * @param sorted the sorted to set
     */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

}
