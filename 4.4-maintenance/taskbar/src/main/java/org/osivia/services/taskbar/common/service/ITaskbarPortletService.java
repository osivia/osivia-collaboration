package org.osivia.services.taskbar.common.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;

/**
 * Taskbar portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ITaskbarPortletService {

    /**
     * Get tasks.
     *
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext) throws PortletException;


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
     * @param id task identifier
     * @return task
     * @throws PortletException
     */
    Task start(PortalControllerContext portalControllerContext, String id) throws PortletException;


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
     * Get taskbar configuration.
     *
     * @param portalControllerContext portlet controller context
     * @return taskbar configuration
     * @throws PortletException
     */
    TaskbarConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save taskbar configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration taskbar configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, TaskbarConfiguration configuration) throws PortletException;

}
