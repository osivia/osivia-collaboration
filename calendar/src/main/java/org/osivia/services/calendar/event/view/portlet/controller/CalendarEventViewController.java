package org.osivia.services.calendar.event.view.portlet.controller;

import java.util.Locale;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.calendar.event.view.portlet.model.CalendarEventViewForm;
import org.osivia.services.calendar.event.view.portlet.service.CalendarEventViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Calendar event view portlet controller.
 * 
 * @author Julien Barberet
 */
@Controller
@RequestMapping(value = "VIEW")
public class CalendarEventViewController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private CalendarEventViewService service;

    /**
     * Constructor.
     */
	public CalendarEventViewController() {
		super();
	}


	/**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param options calendar edition options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        response.setTitle(this.service.getTitle(portalControllerContext));
        
        this.service.insertContentMenubarItems(portalControllerContext);
        return "view";
    }
    
    

    /**
     * Get calendar event edition form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return calendar event edition form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public CalendarEventViewForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
