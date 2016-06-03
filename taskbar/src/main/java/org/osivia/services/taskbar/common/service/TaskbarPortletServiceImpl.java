package org.osivia.services.taskbar.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.panels.IPanelsService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarItems;
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
        // Identifiers of remaining tasks
        List<String> remainingIds = new ArrayList<String>(order);
        remainingIds.remove(ITaskbarRepository.CMS_NAVIGATION_TASK_ID);

        // Navigation tasks
        List<TaskbarTask> navigationTasks = this.taskbarRepository.getNavigationTasks(portalControllerContext);
        List<TaskbarTask> displayedTasks = new ArrayList<TaskbarTask>(navigationTasks.size());
        for (TaskbarTask navigationTask : navigationTasks) {
            if (TaskbarItemType.TRANSVERSAL.equals(navigationTask.getType())) {
                remainingIds.remove(navigationTask.getId());
            }

            if (!navigationTask.isDisabled()) {
                displayedTasks.add(navigationTask);
            }
        }


        // Remaining tasks
        if (!remainingIds.isEmpty()) {
            TaskbarItems items = this.taskbarRepository.getTaskbarItems(portalControllerContext);
            List<TaskbarTask> remainingTasks = new ArrayList<TaskbarTask>(remainingIds.size());
            boolean before = true;
            for (String id : order) {
                if (ITaskbarRepository.CMS_NAVIGATION_TASK_ID.equals(id)) {
                    displayedTasks.addAll(0, remainingTasks);
                    remainingTasks.clear();
                    before = false;
                } else if (remainingIds.contains(id)) {
                    TaskbarItem item = items.get(id);
                    if (item != null) {
                        TaskbarTask remainingTask = this.taskbarRepository.createRemainingTask(portalControllerContext, item);
                        remainingTasks.add(remainingTask);
                    }
                }
            }
            if (before) {
                displayedTasks.addAll(0, remainingTasks);
            } else {
                displayedTasks.addAll(remainingTasks);
            }
        }


        // Tasks
        List<Task> tasks = new ArrayList<Task>(displayedTasks.size());
        for (TaskbarTask displayedTask : displayedTasks) {
            Task task = new Task(displayedTask);
            tasks.add(task);
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
    public List<TaskbarItem> getOrderedItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        TaskbarConfiguration configuration = this.taskbarRepository.getConfiguration(portalControllerContext);
        // Order
        List<String> order = configuration.getOrder();

        // Taskbar items
        TaskbarItems items = this.taskbarRepository.getTaskbarItems(portalControllerContext);

        // Ordered taskbar items
        List<TaskbarItem> orderedItems = new ArrayList<TaskbarItem>();
        for (String id : order) {
            TaskbarItem item;
            if (ITaskbarRepository.CMS_NAVIGATION_TASK_ID.equals(id)) {
                item = this.taskbarRepository.createVirtualItem(portalControllerContext);
            } else {
                item = items.get(id);
                if ((item != null) && !TaskbarItemType.TRANSVERSAL.equals(item.getType())) {
                    item = null;
                }
            }

            if (item != null) {
                orderedItems.add(item);
            }
        }

        return orderedItems;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskbarItem> getAvailableItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        TaskbarConfiguration configuration = this.taskbarRepository.getConfiguration(portalControllerContext);
        // Order
        List<String> order = configuration.getOrder();

        // Taskbar items
        TaskbarItems items = this.taskbarRepository.getTaskbarItems(portalControllerContext);

        // Available taskbar items
        List<TaskbarItem> availableItems = new ArrayList<TaskbarItem>();
        if (!order.contains(ITaskbarRepository.CMS_NAVIGATION_TASK_ID)) {
            TaskbarItem item = this.taskbarRepository.createVirtualItem(portalControllerContext);
            availableItems.add(item);
        }
        for (TaskbarItem item : items.getAll()) {
            if (TaskbarItemType.TRANSVERSAL.equals(item.getType()) && !order.contains(item.getId())) {
                availableItems.add(item);
            }
        }

        return availableItems;
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
