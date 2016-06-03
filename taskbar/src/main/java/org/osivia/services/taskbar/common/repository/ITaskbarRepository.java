package org.osivia.services.taskbar.common.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;

/**
 * Taskbar repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ITaskbarRepository {

    /** CMS navigation virtual task identifier. */
    String CMS_NAVIGATION_TASK_ID = "CMS_NAVIGATION";


    /**
     * Get taskbar configuration.
     *
     * @param portalControllerContext portal controller context
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


    /**
     * Get navigation taskbar tasks.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar tasks
     * @throws PortletException
     */
    List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Create remaining task.
     *
     * @param portalControllerContext portal controller context
     * @param taskbarItem taskbar item
     * @return taskbar task
     * @throws PortletException
     */
    TaskbarTask createRemainingTask(PortalControllerContext portalControllerContext, TaskbarItem item) throws PortletException;


    /**
     * Get taskbar items.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar items
     * @throws PortletException
     */
    TaskbarItems getTaskbarItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update tasks.
     *
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @throws PortletException
     */
    void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException;


    /**
     * Create virtual taskbar item.
     *
     * @param portalControllerContext portal controller context
     * @return taskbar item
     * @throws PortletException
     */
    TaskbarItem createVirtualItem(PortalControllerContext portalControllerContext) throws PortletException;

}
