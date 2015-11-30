package org.osivia.services.taskbar.portlet.controller;

import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;
import org.osivia.services.taskbar.common.model.TaskbarView;
import org.osivia.services.taskbar.common.service.ITaskbarPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Taskbar admin controller.
 *
 * @author Cédric Krommenhoek
 * @see PortalGenericPortlet
 */
@Controller
@RequestMapping(value = "ADMIN")
public class TaskbarAdminController extends PortalGenericPortlet implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** Taskbar portlet service. */
    @Autowired
    private ITaskbarPortletService taskbarService;


    /**
     * Constructor.
     */
    public TaskbarAdminController() {
        super();
    }


    /**
     * Render mapping.
     *
     * @param request render request
     * @param response render response
     * @return admin path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Ordered tasks
        List<Task> orderedTasks = this.taskbarService.getOrderedTasks(portalControllerContext);
        request.setAttribute("orderedTasks", orderedTasks);

        // Available tasks
        List<Task> availableTasks = this.taskbarService.getAvailableTasks(portalControllerContext);
        request.setAttribute("availableTasks", availableTasks);

        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param configuration taskbar configuration model attribute
     * @param order tasks order request parameter
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute(value = "configuration") TaskbarConfiguration configuration,
            @RequestParam(value = "order") String order) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tasks order
        configuration.setOrder(Arrays.asList(StringUtils.split(order, "|")));

        this.taskbarService.saveConfiguration(portalControllerContext, configuration);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get taskbar configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return taskbar configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "configuration")
    public TaskbarConfiguration getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.taskbarService.getConfiguration(portalControllerContext);
    }


    /**
     * Get tasks order model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param configuration taskbar configuration model attribute
     * @return task order
     */
    @ModelAttribute(value = "order")
    public String getOrder(PortletRequest request, PortletResponse response, @ModelAttribute(value = "configuration") TaskbarConfiguration configuration) {
        return StringUtils.join(configuration.getOrder(), "|");
    }


    /**
     * Get taskbar views model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return taskbar views
     * @throws PortletException
     */
    @ModelAttribute(value = "views")
    public List<TaskbarView> getViews(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(TaskbarView.values());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
