package org.osivia.services.taskbar.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.panels.IPanelsService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;
import org.osivia.services.taskbar.common.repository.ITaskbarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Taskbar portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see ITaskbarPortletService
 */
@Service
public class TaskbarPortletServiceImpl implements ITaskbarPortletService {

    /** Taskbar repository. */
    @Autowired
    private ITaskbarRepository taskbarRepository;

    /** Panels service. */
    private final IPanelsService panelsService;


    /**
     * Constructor.
     */
    public TaskbarPortletServiceImpl() {
        super();

        // Panels service
        this.panelsService = Locator.findMBean(IPanelsService.class, IPanelsService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        TaskbarConfiguration configuration = this.taskbarRepository.getConfiguration(portalControllerContext);
        // Order
        List<String> order = configuration.getOrder();

        // CMS tasks
        List<TaskbarTask> cmsTasks = this.taskbarRepository.getNavigationTasks(portalControllerContext);

        // Custom tasks
        List<TaskbarTask> customTasks = this.taskbarRepository.getCustomTasks(portalControllerContext);
        Map<String, TaskbarTask> customTasksMap = new HashMap<String, TaskbarTask>(customTasks.size());
        for (TaskbarTask customTask : customTasks) {
            customTasksMap.put(customTask.getId(), customTask);
        }


        // Tasks
        List<Task> tasks = new ArrayList<Task>(Math.max((cmsTasks.size() + order.size()) - 1, 0));
        for (String id : order) {
            if (CMS_NAVIGATION_TASK_ID.equals(id)) {
                for (TaskbarTask cmsTask : cmsTasks) {
                    Task task = new Task(cmsTask);
                    tasks.add(task);
                }
            } else {
                TaskbarTask customTask = customTasksMap.get(id);
                if (customTask != null) {
                    Task task = new Task(customTask);
                    tasks.add(task);
                }
            }
        }

        return tasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException {
        this.taskbarRepository.updateTasks(portalControllerContext, tasks);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Task start(PortalControllerContext portalControllerContext, String id) throws PortletException {
        Task result = null;

        List<Task> tasks = this.getTasks(portalControllerContext);
        for (Task task : tasks) {
            if (id.equals(task.getId())) {
                result = task;
                break;
            }
        }

        if (result != null) {
            // Update task
            List<Task> results = new ArrayList<Task>(1);
            results.add(result);
            this.taskbarRepository.updateTasks(portalControllerContext, results);
        }

        // Reset task dependent panels
        try {
            this.panelsService.resetTaskDependentPanels(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getOrderedTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        TaskbarConfiguration configuration = this.taskbarRepository.getConfiguration(portalControllerContext);
        // Order
        List<String> order = configuration.getOrder();
        // Custom tasks
        List<TaskbarTask> customTasks = this.taskbarRepository.getCustomTasks(portalControllerContext);
        Map<String, TaskbarTask> customTasksMap = new HashMap<String, TaskbarTask>(customTasks.size());
        for (TaskbarTask customTask : customTasks) {
            customTasksMap.put(customTask.getId(), customTask);
        }

        // Ordered tasks
        List<Task> orderedTasks = new ArrayList<Task>(order.size());
        for (String id : order) {
            Task task;
            if (CMS_NAVIGATION_TASK_ID.equals(id)) {
                task = this.createNavigationTask();
            } else {
                TaskbarTask customTask = customTasksMap.get(id);
                if (customTask != null) {
                    task = new Task(customTask);
                } else {
                    task = null;
                }
            }

            if (task != null) {
                this.taskbarRepository.updateTaskName(portalControllerContext, task);
                orderedTasks.add(task);
            }
        }

        return orderedTasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getAvailableTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        TaskbarConfiguration configuration = this.taskbarRepository.getConfiguration(portalControllerContext);
        // Order
        List<String> order = configuration.getOrder();
        // Custom tasks
        List<TaskbarTask> customTasks = this.taskbarRepository.getCustomTasks(portalControllerContext);

        // Available tasks
        List<Task> availableTasks = new ArrayList<Task>(customTasks.size() + 1);

        if (!order.contains(CMS_NAVIGATION_TASK_ID)) {
            Task task = this.createNavigationTask();
            this.taskbarRepository.updateTaskName(portalControllerContext, task);
            availableTasks.add(task);
        }

        for (TaskbarTask customTask : customTasks) {
            if (!order.contains(customTask.getId())) {
                Task task = new Task(customTask);
                this.taskbarRepository.updateTaskName(portalControllerContext, task);
                availableTasks.add(task);
            }
        }

        return availableTasks;
    }


    /**
     * Create CMS navigation virtual task.
     *
     * @return task
     */
    private Task createNavigationTask() {
        Task task = new Task();
        task.setId(CMS_NAVIGATION_TASK_ID);
        task.setKey("CMS_NAVIGATION_TASK");
        task.setIcon("glyphicons glyphicons-magic");
        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        return this.taskbarRepository.getConfiguration(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, TaskbarConfiguration configuration) throws PortletException {
        this.taskbarRepository.saveConfiguration(portalControllerContext, configuration);
    }

}
