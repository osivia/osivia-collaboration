package org.osivia.services.calendar.view.portlet.controller;

import java.util.SortedMap;
import java.util.TreeMap;

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

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.calendar.view.portlet.model.CalendarOptions;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Admin calendar controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("ADMIN")
public class AdminCalendarController {

    /** Admin JSP path. */
    private static final String ADMIN_PATH = "admin";

    /** Calendar configuration request attribute. */
    private static final String CALENDAR_CONFIGURATION_ATTRIBUTE = "configuration";
    /** Period types request attribute. */
    private static final String PERIOD_TYPES_ATTRIBUTE = "periodTypes";


    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Calendar repository. */
    @Autowired
    private CalendarViewRepository calendarRepository;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public AdminCalendarController() {
        super();
    }


    /**
     * Admin page render mapping.
     *
     * @param request render request
     * @param response render response
     * @return admin page path
     * @throws PortletException
     */
    @RenderMapping
    public String admin(RenderRequest request, RenderResponse response) throws PortletException {
        return ADMIN_PATH;
    }


    /**
     * Save portlet configuration action.
     *
     * @param request action request
     * @param response action response
     * @param configuration portlet configuration
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void saveConfiguration(ActionRequest request, ActionResponse response, @ModelAttribute(value = "configuration") CalendarOptions configuration)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.calendarRepository.saveConfiguration(portalControllerContext, configuration);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get portlet configuration.
     *
     * @param request portlet request
     * @param response portlet response
     * @return portlet configuration
     * @throws PortletException
     */
    @ModelAttribute(value = CALENDAR_CONFIGURATION_ATTRIBUTE)
    public CalendarOptions getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.calendarRepository.getConfiguration(portalControllerContext);
    }


    /**
     * Get period types.
     *
     * @param request portlet request
     * @param response portlet response
     * @return period types
     * @throws PortletException
     */
    @ModelAttribute(value = PERIOD_TYPES_ATTRIBUTE)
    public SortedMap<String, String> getPeriodTypes(PortletRequest request, PortletResponse response) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        SortedMap<String, String> periodTypes = new TreeMap<String, String>();
        for (PeriodTypes periodType : PeriodTypes.values()) {
            periodTypes.put(periodType.getName(), bundle.getString(periodType.getInternationalizationKey()));
        }
        return periodTypes;
    }

}
