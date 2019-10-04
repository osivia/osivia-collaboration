package org.osivia.services.calendar.integration.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.integration.portlet.model.CalendarIntegrationForm;
import org.osivia.services.calendar.integration.portlet.service.CalendarIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

/**
 * Calendar integration portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class CalendarIntegrationController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private CalendarIntegrationService service;


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "view";
    }


    /**
     * Get calendar integration form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public CalendarIntegrationForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
