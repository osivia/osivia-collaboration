package org.osivia.services.taskbar.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.taskbar.common.model.Task;
import org.osivia.services.taskbar.common.model.TaskbarConfiguration;
import org.osivia.services.taskbar.common.service.ITaskbarPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Taskbar view controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class TaskbarViewController extends CMSPortlet implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** Taskbar portlet service. */
    @Autowired
    private ITaskbarPortletService taskbarService;


    /**
     * Constructor.
     */
    public TaskbarViewController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param tasks tasks model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tasks
        List<Task> tasks = this.taskbarService.getTasks(portalControllerContext);
        this.taskbarService.updateTasks(portalControllerContext, tasks);
        request.setAttribute("tasks", tasks);

        // Taskbar view
        TaskbarConfiguration configuration = this.taskbarService.getConfiguration(portalControllerContext);
        return configuration.getView().getPath();
    }


    /**
     * Start task.
     *
     * @param request action request
     * @param response action response
     * @param id task identifier required request parameter
     * @param tasks tasks model attribute
     * @throws IOException
     * @throws PortletException
     */
    @ActionMapping(value = "start")
    public void start(ActionRequest request, ActionResponse response, @RequestParam(value = "id") String id) throws IOException, PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Task
        Task task = this.taskbarService.start(portalControllerContext, id);

        // Redirection
        response.sendRedirect(task.getUrl());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
