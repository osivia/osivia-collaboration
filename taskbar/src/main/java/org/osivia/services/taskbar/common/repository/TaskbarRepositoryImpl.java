package org.osivia.services.taskbar.common.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;
import org.osivia.services.taskbar.common.model.TaskbarView;
import org.osivia.services.taskbar.common.service.ITaskbarPortletService;
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
        orderedTasks.add(ITaskbarPortletService.CMS_NAVIGATION_TASK_ID);
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
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        // Base path
        String basePath = nuxeoController.getBasePath();

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (StringUtils.isEmpty(basePath)) {
            navigationTasks = new ArrayList<TaskbarTask>(0);
        } else {
            try {
                navigationTasks = this.taskbarService.getNavigationTasks(portalControllerContext, basePath);
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
    public List<TaskbarTask> getCustomTasks(PortalControllerContext portalControllerContext) throws PortletException {
        try {
            return this.taskbarService.getCustomTasks(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTasks(PortalControllerContext portalControllerContext, List<Task> tasks) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);

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

            // Internationalized name
            this.updateTaskName(portalControllerContext, task);


            // Home task
            if (ITaskbarService.HOME_TASK_ID.equals(task.getId())) {
                task.setPath(nuxeoController.getBasePath());
            }


            if (task.getPath() != null) {
                // CMS URL
                String url = this.portalURLFactory.getCMSUrl(portalControllerContext, null, task.getPath(), null, null, "taskbar", null, null, "1", null);
                task.setUrl(url);
            } else if (task.getMaximizedPlayer() != null) {
                // Start portlet URL
                PanelPlayer player = task.getMaximizedPlayer();

                // Window properties
                Map<String, String> properties = new HashMap<String, String>();
                if (player.getProperties() != null) {
                    properties.putAll(player.getProperties());
                }
                properties.put(ITaskbarService.TASK_ID_WINDOW_PROPERTY, task.getId());
                if (task.getName() != null) {
                    properties.put("osivia.title", task.getName());
                }
                properties.put("osivia.back.reset", String.valueOf(true));
                properties.put("osivia.navigation.reset", String.valueOf(true));

                try {
                    String url = this.portalURLFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties, false);
                    task.setUrl(url);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTaskName(PortalControllerContext portalControllerContext, Task task) throws PortletException {
        if (task.getKey() != null) {
            // Bundle
            Locale locale = portalControllerContext.getRequest().getLocale();
            Bundle bundle = this.bundleFactory.getBundle(locale);

            String name = bundle.getString(task.getKey());
            if (name != null) {
                task.setName(name);
            }
        }
    }


    /**
     * Get Nuxeo controller.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet context
        PortletContext portletContext = portalControllerContext.getPortletCtx();

        return new NuxeoController(request, response, portletContext);
    }

}
