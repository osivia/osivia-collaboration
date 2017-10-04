package org.osivia.services.taskbar.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.taskbar.portlet.model.Task;
import org.osivia.services.taskbar.portlet.model.TaskbarSettings;
import org.osivia.services.taskbar.portlet.model.TaskbarView;
import org.osivia.services.taskbar.portlet.repository.command.CreateSearchStapleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Taskbar portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see TaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements TaskbarRepository {

    /** Tasks order window property name. */
    private static final String ORDER_WINDOW_PROPERTY = "osivia.taskbar.order";
    /** Taskbar view window property name. */
    private static final String VIEW_WINDOW_PROPERTY = "osivia.taskbar.view";

    /** Taskbar URL display context. */
    private static final String TASKBAR_DISPLAY_CONTEXT = "taskbar";

    /** Tasks order separator. */
    private static final String ORDER_SEPARATOR = "|";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public TaskbarRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Taskbar settings
        TaskbarSettings settings = new TaskbarSettings();

        // Tasks order
        List<String> order;
        String orderProperty = window.getProperty(ORDER_WINDOW_PROPERTY);
        if (orderProperty == null) {
            order = this.initOrder();
            window.setProperty(ORDER_WINDOW_PROPERTY, StringUtils.join(order, ORDER_SEPARATOR));
        } else {
            order = Arrays.asList(StringUtils.split(orderProperty, ORDER_SEPARATOR));
        }
        settings.setOrder(order);

        // Taskbar view
        TaskbarView view = TaskbarView.fromName(window.getProperty(VIEW_WINDOW_PROPERTY));
        settings.setView(view);

        return settings;
    }


    /**
     * Initialize tasks order.
     *
     * @return tasks order
     */
    private List<String> initOrder() {
        List<String> orderedTasks = new ArrayList<String>(2);
        orderedTasks.add(ITaskbarService.HOME_TASK_ID);
        orderedTasks.add(TaskbarRepository.CMS_NAVIGATION_TASK_ID);
        return orderedTasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings(PortalControllerContext portalControllerContext, TaskbarSettings settings) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Tasks order
        window.setProperty(ORDER_WINDOW_PROPERTY, StringUtils.join(settings.getOrder(), ORDER_SEPARATOR));

        // View
        window.setProperty(VIEW_WINDOW_PROPERTY, settings.getView().getName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Base path
        String basePath = nuxeoController.getBasePath();

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (StringUtils.isEmpty(basePath)) {
            navigationTasks = new ArrayList<TaskbarTask>(0);
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, basePath, true);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return navigationTasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarTask createRemainingTask(PortalControllerContext portalControllerContext, TaskbarItem item) throws PortletException {
        // Factory
        TaskbarFactory factory = this.taskbarService.getFactory();

        return factory.createTaskbarTask(item, null, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItems getTaskbarItems(PortalControllerContext portalControllerContext) throws PortletException {
        TaskbarItems items;
        try {
            items = this.taskbarService.getItems(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        return items;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Active task identifier
        String activeId;
        try {
            activeId = this.taskbarService.getActiveId(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        for (Task task : tasks) {
            // Active indicator
            task.setActive(StringUtils.equals(activeId, task.getId()));

            // Display name
            String displayName;
            if (task.getKey() != null) {
                displayName = bundle.getString(task.getKey(), task.getCustomizedClassLoader());
            } else {
                displayName = task.getTitle();
            }
            task.setDisplayName(displayName);


            if (ITaskbarService.SEARCH_TASK_ID.equals(task.getId()) && TaskbarItemType.STAPLED.equals(task.getType()) && StringUtils.isEmpty(task.getPath())) {
                // Search staple creation
                this.createSearchStaple(portalControllerContext, task);
            }

            // URL
            String url;
            if (ITaskbarService.HOME_TASK_ID.equals(task.getId())) {
                // Home task
                url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, nuxeoController.getBasePath(), null, null, TASKBAR_DISPLAY_CONTEXT, null,
                        null, null, null);
            } else if (task.getPlayer() != null) {
                // Start portlet URL
                PanelPlayer player = task.getPlayer();

                // Window properties
                Map<String, String> properties = new HashMap<String, String>();
                if (player.getProperties() != null) {
                    properties.putAll(player.getProperties());
                }
                properties.put(ITaskbarService.TASK_ID_WINDOW_PROPERTY, task.getId());
                if (task.getDisplayName() != null) {
                    properties.put("osivia.title", task.getDisplayName());
                }
                properties.put("osivia.back.reset", String.valueOf(true));
                properties.put("osivia.navigation.reset", String.valueOf(true));

                try {
                    url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }
            } else if (StringUtils.isNotEmpty(task.getPath())) {
                // CMS URL
                url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, task.getPath(), null, null, TASKBAR_DISPLAY_CONTEXT, null, null, null,
                        null);
            } else {
                // Unknown case
                url = "#";
            }
            task.setUrl(url);
        }
    }


    /**
     * Create search staple.
     * 
     * @param portalControllerContext portal controller context
     * @param task taskbar task
     */
    private void createSearchStaple(PortalControllerContext portalControllerContext, Task task) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Base path
        String basePath = nuxeoController.getBasePath();

        if (StringUtils.isNotEmpty(basePath)) {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(CreateSearchStapleCommand.class, basePath, task);
            Document document = (Document) nuxeoController.executeNuxeoCommand(command);

            // Update task
            task.setPath(document.getPath());

            // Update navigation
            request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, String.valueOf(true));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarItem createVirtualItem(PortalControllerContext portalControllerContext) throws PortletException {
        // Factory
        TaskbarFactory factory = this.taskbarService.getFactory();

        // Taskbar item
        return factory.createTransversalTaskbarItem(CMS_NAVIGATION_TASK_ID, "CMS_NAVIGATION_TASK", "glyphicons glyphicons-magic", null);
    }

}
