package org.osivia.services.taskbar.common.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;

/**
 * Taskbar repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ITaskbarRepository {

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
     * Get CMS tasks.
     *
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get customized tasks.
     *
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<TaskbarTask> getCustomTasks(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update tasks.
     *
     * @param portalControllerContext portal controller context
     * @param tasks tasks
     * @throws PortletException
     */
    void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException;


    /**
     * Update task name.
     *
     * @param portalControllerContext portal controller context
     * @param task task
     * @throws PortletException
     */
    void updateTaskName(PortalControllerContext portalControllerContext, Task task) throws PortletException;

}
