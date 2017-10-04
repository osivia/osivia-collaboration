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
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.services.taskbar.portlet.model.TaskbarSettings;
import org.osivia.services.taskbar.portlet.model.TaskbarView;
import org.osivia.services.taskbar.portlet.service.TaskbarPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Taskbar admin controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("ADMIN")
public class TaskbarAdminController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private TaskbarPortletService service;


    /**
     * Constructor.
     */
    public TaskbarAdminController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param settings taskbar settings model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("settings") TaskbarSettings settings) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Order
        String order = StringUtils.join(settings.getOrder(), "|");
        request.setAttribute("order", order);

        // Ordered tasks
        List<TaskbarItem> orderedTasks = this.service.getOrderedItems(portalControllerContext);
        request.setAttribute("orderedItems", orderedTasks);

        // Available tasks
        List<TaskbarItem> availableTasks = this.service.getAvailableItems(portalControllerContext);
        request.setAttribute("availableItems", availableTasks);

        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param order tasks order request parameter
     * @param settings taskbar settings model attribute
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @RequestParam("order") String order, @ModelAttribute("settings") TaskbarSettings settings)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tasks order
        settings.setOrder(Arrays.asList(StringUtils.split(order, "|")));

        this.service.saveSettings(portalControllerContext, settings);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get taskbar settings model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return taskbar settings
     * @throws PortletException
     */
    @ModelAttribute("settings")
    public TaskbarSettings getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSettings(portalControllerContext);
    }


    /**
     * Get taskbar views model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return taskbar views
     * @throws PortletException
     */
    @ModelAttribute("views")
    public List<TaskbarView> getViews(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(TaskbarView.values());
    }

}
