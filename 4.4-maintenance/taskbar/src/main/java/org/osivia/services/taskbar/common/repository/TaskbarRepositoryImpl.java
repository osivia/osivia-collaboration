package org.osivia.services.taskbar.common.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;
import org.osivia.services.taskbar.common.model.TaskbarView;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Taskbar repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see ITaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements ITaskbarRepository {

    /** Tasks order window property name. */
    private static final String ORDER_WINDOW_PROPERTY = "osivia.taskbar.order";
    /** Taskbar view window property name. */
    private static final String VIEW_WINDOW_PROPERTY = "osivia.taskbar.view";

    /** Tasks order separator. */
    private static final String ORDER_SEPARATOR = "|";


    /** Taskbar service. */
    private final ITaskbarService taskbarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalURLFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public TaskbarRepositoryImpl() {
        super();

        // Taskbar service
        this.taskbarService = Locator.findMBean(ITaskbarService.class, ITaskbarService.MBEAN_NAME);
        // Portal URL factory
        this.portalURLFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskbarConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Taskbar configuration
        TaskbarConfiguration configuration = new TaskbarConfiguration();

        // Tasks order
        List<String> order;
        String orderProperty = window.getProperty(ORDER_WINDOW_PROPERTY);
        if (orderProperty == null) {
            order = this.initOrder();
            window.setProperty(ORDER_WINDOW_PROPERTY, StringUtils.join(order, ORDER_SEPARATOR));
        } else {
            order = Arrays.asList(StringUtils.split(orderProperty, ORDER_SEPARATOR));
        }
        configuration.setOrder(order);

        // Taskbar view
        TaskbarView view = TaskbarView.fromName(window.getProperty(VIEW_WINDOW_PROPERTY));
        configuration.setView(view);

        return configuration;
    }


    /**
     * Initialize tasks order.
     *
     * @return tasks order
     */
    private List<String> initOrder() {
        List<String> orderedTasks = new ArrayList<String>(2);
        orderedTasks.add(ITaskbarService.HOME_TASK_ID);
        orderedTasks.add(ITaskbarRepository.CMS_NAVIGATION_TASK_ID);
        return orderedTasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, TaskbarConfiguration configuration) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Tasks order
        window.setProperty(ORDER_WINDOW_PROPERTY, StringUtils.join(configuration.getOrder(), ORDER_SEPARATOR));

        // View
        window.setProperty(VIEW_WINDOW_PROPERTY, configuration.getView().getName());
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

            // URL
            String url;
            if (ITaskbarService.HOME_TASK_ID.equals(task.getId())) {
                // Home task
                url = this.portalURLFactory.getCMSUrl(portalControllerContext, null, nuxeoController.getBasePath(), null, null, "taskbar", null, null, "1",
                        null);
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
                    url = this.portalURLFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }
            } else if (task.getPath() != null) {
                // CMS URL
                url = this.portalURLFactory.getCMSUrl(portalControllerContext, null, task.getPath(), null, null, "taskbar", null, null, "1", null);
            } else {
                // Unknown case
                url = "#";
            }
            task.setUrl(url);
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
