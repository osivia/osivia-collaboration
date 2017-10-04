package org.osivia.services.taskbar.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.services.taskbar.portlet.model.Task;
import org.osivia.services.taskbar.portlet.model.TaskbarSettings;

/**
 * Taskbar portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TaskbarPortletService {

    /**
     * Get tasks.
     *
     * @param portalControllerContext portal controller context
     * @param settings taskbar settings
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext, TaskbarSettings settings) throws PortletException;


    /**
     * Update tasks.
     *
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @throws PortletException
     */
    void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException;


    /**
     * Start task.
     *
     * @param portalControllerContext portal controller context
     * @param settings taskbar settings
     * @param id task identifier
     * @return task
     * @throws PortletException
     */
    Task start(PortalControllerContext portalControllerContext, TaskbarSettings settings, String id) throws PortletException;


    /**
     * Get ordonned taskbar items.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar items
     * @throws PortletException
     */
    List<TaskbarItem> getOrderedItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get available taskbar items.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar items
     * @throws PortletException
     */
    List<TaskbarItem> getAvailableItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get taskbar settings.
     *
     * @param portalControllerContext portlet controller context
     * @return taskbar settings
     * @throws PortletException
     */
    TaskbarSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save taskbar settings.
     *
     * @param portalControllerContext portal controller context
     * @param configuration taskbar settings
     * @throws PortletException
     */
    void saveSettings(PortalControllerContext portalControllerContext, TaskbarSettings configuration) throws PortletException;

}
