package org.osivia.services.taskbar.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.taskbar.portlet.model.Task;
import org.osivia.services.taskbar.portlet.model.TaskbarSettings;
import org.osivia.services.taskbar.portlet.service.TaskbarPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Taskbar view controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping(value = "VIEW")
public class TaskbarViewController extends CMSPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private TaskbarPortletService service;


    /**
     * Constructor.
     */
    public TaskbarViewController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
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

        // Tasks
        List<Task> tasks = this.service.getTasks(portalControllerContext, settings);
        this.service.updateTasks(portalControllerContext, tasks);
        request.setAttribute("tasks", tasks);


        return settings.getView().getPath();
    }


    /**
     * Start task action mapping.
     *
     * @param request action request
     * @param response action response
     * @param id task identifier required request parameter
     * @param settings taskbar settings model attribute
     * @throws IOException
     * @throws PortletException
     */
    @ActionMapping(value = "start")
    public void start(ActionRequest request, ActionResponse response, @RequestParam(value = "id") String id,
            @ModelAttribute("settings") TaskbarSettings settings) throws IOException, PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Task
        Task task = this.service.start(portalControllerContext, settings, id);

        // Redirection
        response.sendRedirect(task.getUrl());
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
    public TaskbarSettings getSettings(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSettings(portalControllerContext);
    }

}
