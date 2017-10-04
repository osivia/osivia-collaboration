package org.osivia.services.taskbar.portlet.model;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.TaskbarItemRestriction;
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


    /** Path. */
    private String path;


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
    public String getTemplate() {
        return this.task.getTemplate();
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
        return StringUtils.defaultIfEmpty(this.path, this.task.getPath());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisabled() {
        return this.task.isDisabled();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItemRestriction getRestriction() {
        return this.task.getRestriction();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHidden() {
        return this.task.isHidden();
    }


    /**
     * Setter for path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

}
