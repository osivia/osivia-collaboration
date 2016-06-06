package org.osivia.services.taskbar.common.model;

import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarTask;

/**
 * Taskbar task decorator.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarTask
 */
public abstract class TaskbarTaskDecorator implements TaskbarTask {

    /** Taskbar task. */
    private final TaskbarTask task;


    /**
     * Constructor.
     *
     * @param task taskbar task
     */
    public TaskbarTaskDecorator(TaskbarTask task) {
        super();
        this.task = task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.task.getId();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItemType getType() {
        return this.task.getType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return this.task.getKey();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getCustomizedClassLoader() {
        return this.task.getCustomizedClassLoader();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon() {
        return this.task.getIcon();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PanelPlayer getPlayer() {
        return this.task.getPlayer();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentType() {
        return this.task.getDocumentType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDefault() {
        return this.task.isDefault();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return this.task.getOrder();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.task.getTitle();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.task.getPath();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return this.task.isDisabled();
    }

}
