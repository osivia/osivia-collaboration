package org.osivia.services.taskbar.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.IPanelsService;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.taskbar.portlet.model.Task;
import org.osivia.services.taskbar.portlet.model.TaskbarSettings;
import org.osivia.services.taskbar.portlet.repository.TaskbarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Taskbar portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarPortletService
 */
@Service
public class TaskbarPortletServiceImpl implements TaskbarPortletService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private TaskbarRepository repository;

    /** Panels service. */
    @Autowired
    private IPanelsService panelsService;


    /**
     * Constructor.
     */
    public TaskbarPortletServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(PortalControllerContext portalControllerContext, TaskbarSettings settings) throws PortletException {
        // Order
        List<String> order = settings.getOrder();
        // Identifiers of remaining tasks
        List<String> remainingIds = new ArrayList<String>(order);
        remainingIds.remove(TaskbarRepository.CMS_NAVIGATION_TASK_ID);

        // Navigation tasks
        List<TaskbarTask> navigationTasks = this.repository.getNavigationTasks(portalControllerContext);
        List<TaskbarTask> displayedTasks = new ArrayList<TaskbarTask>(navigationTasks.size());
        TaskbarTask searchTask = null;
        for (TaskbarTask navigationTask : navigationTasks) {
            if (TaskbarItemType.TRANSVERSAL.equals(navigationTask.getType())) {
                remainingIds.remove(navigationTask.getId());
            }

            if (!navigationTask.isDisabled() && !navigationTask.isHidden()) {
                displayedTasks.add(navigationTask);
            }

            if (ITaskbarService.SEARCH_TASK_ID.equals(navigationTask.getId())) {
                searchTask = navigationTask;
            }
        }


        // Remaining tasks
        if (!remainingIds.isEmpty()) {
            TaskbarItems items = this.repository.getTaskbarItems(portalControllerContext);
            List<TaskbarTask> remainingTasks = new ArrayList<TaskbarTask>(remainingIds.size());
            boolean before = true;
            for (String id : order) {
                if (TaskbarRepository.CMS_NAVIGATION_TASK_ID.equals(id)) {
                    displayedTasks.addAll(0, remainingTasks);
                    remainingTasks.clear();
                    before = false;
                } else if (remainingIds.contains(id)) {
                    TaskbarItem item = items.get(id);
                    if (ITaskbarService.SEARCH_TASK_ID.equals(id) && (searchTask != null)) {
                        // Search task
                        remainingTasks.add(searchTask);
                    } else if (item != null) {
                        TaskbarTask remainingTask = this.repository.createRemainingTask(portalControllerContext, item);
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
            Task task = this.applicationContext.getBean(Task.class, displayedTask);
            tasks.add(task);
        }

        return tasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException {
        this.repository.updateTasks(portalControllerContext, tasks);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Task start(PortalControllerContext portalControllerContext, TaskbarSettings settings, String id) throws PortletException {
        Task result = null;

        List<Task> tasks = this.getTasks(portalControllerContext, settings);
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
            this.repository.updateTasks(portalControllerContext, results);
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
        // Settings
        TaskbarSettings settings = this.repository.getSettings(portalControllerContext);
        // Order
        List<String> order = settings.getOrder();

        // Taskbar items
        TaskbarItems items = this.repository.getTaskbarItems(portalControllerContext);

        // Ordered taskbar items
        List<TaskbarItem> orderedItems = new ArrayList<TaskbarItem>();
        for (String id : order) {
            TaskbarItem item;
            if (TaskbarRepository.CMS_NAVIGATION_TASK_ID.equals(id)) {
                item = this.repository.createVirtualItem(portalControllerContext);
            } else {
                item = items.get(id);
                if ((item != null) && !TaskbarItemType.TRANSVERSAL.equals(item.getType()) && !ITaskbarService.SEARCH_TASK_ID.equals(id)) {
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
        // Settings
        TaskbarSettings settings = this.repository.getSettings(portalControllerContext);
        // Order
        List<String> order = settings.getOrder();

        // Taskbar items
        TaskbarItems items = this.repository.getTaskbarItems(portalControllerContext);

        // Available taskbar items
        List<TaskbarItem> availableItems = new ArrayList<TaskbarItem>();
        if (!order.contains(TaskbarRepository.CMS_NAVIGATION_TASK_ID)) {
            TaskbarItem item = this.repository.createVirtualItem(portalControllerContext);
            availableItems.add(item);
        }
        for (TaskbarItem item : items.getAll()) {
            if ((TaskbarItemType.TRANSVERSAL.equals(item.getType()) || ITaskbarService.SEARCH_TASK_ID.equals(item.getId())) && !order.contains(item.getId())) {
                availableItems.add(item);
            }
        }

        return availableItems;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getSettings(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings(PortalControllerContext portalControllerContext, TaskbarSettings settings) throws PortletException {
        this.repository.saveSettings(portalControllerContext, settings);
    }

}
