package org.osivia.services.taskbar.empty.controller;

import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Taskbar empty window view controller.
 *
 * @author Cédric Krommenhoek
 * @see PortalGenericPortlet
 */
@Controller
@RequestMapping(value = "VIEW")
public class TaskbarEmptyViewController extends PortalGenericPortlet {

    /**
     * Constructor.
     */
    public TaskbarEmptyViewController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "empty";
    }

}
