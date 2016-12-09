package org.osivia.services.workspace.edition.portlet.model;

import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemRestriction;
import org.osivia.portal.api.taskbar.TaskbarItemType;

/**
 * Taskbar item decorator.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarItem
 */
public abstract class TaskbarItemDecorator implements TaskbarItem {

    /** Taskbar item. */
    private final TaskbarItem item;


    /**
     * Constructor.
     *
     * @param item taskbar item
     */
    public TaskbarItemDecorator(TaskbarItem item) {
        super();
        this.item = item;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.item.getId();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItemType getType() {
        return this.item.getType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return this.item.getKey();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getCustomizedClassLoader() {
        return this.item.getCustomizedClassLoader();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon() {
        return this.item.getIcon();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PanelPlayer getPlayer() {
        return this.item.getPlayer();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTemplate() {
        return this.item.getTemplate();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentType() {
        return this.item.getDocumentType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefault() {
        return this.item.isDefault();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return this.item.getOrder();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setToDefault(int order) {
        // Do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItemRestriction getRestriction() {
        return this.item.getRestriction();
    }

}
