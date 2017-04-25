package org.osivia.services.calendar.portlet.controller;

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

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.service.ICalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View calendar controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletConfigAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewCalendarController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** Error JSP path. */
    private static final String ERROR_PATH = "calendar/error";

    /** Calendar data request attribute. */
    private static final String CALENDAR_DATA_ATTRIBUTE = "calendarData";
    /** Events data request attribute. */
    private static final String EVENTS_DATA_ATTRIBUTE = "eventsData";
    /** Selected date request attribute. */
    private static final String SELECTED_DATE_ATTRIBUTE = ICalendarService.SELECTED_DATE_PARAMETER;
    /** Error message request attribute. */
    private static final String ERROR_MESSAGE_ATTRIBUTE = "message";


    /** Calendar service. */
    @Autowired
    private ICalendarService calendarService;

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /**
     * Default constructor.
     */
    public ViewCalendarController() {
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
     * View calendar render mapping
     *
     * @param request render request
     * @param response render response
     * @param selectedDate request parameter, may be null
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response,
            @RequestParam(value = ICalendarService.SELECTED_DATE_PARAMETER, required = false) String selectedDate, @ModelAttribute CalendarData calendarData)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Selected date
        request.setAttribute(SELECTED_DATE_ATTRIBUTE, selectedDate);

        // Events data
        request.setAttribute(EVENTS_DATA_ATTRIBUTE, this.calendarService.getEventsData(portalControllerContext, calendarData));

        // Title
        String title = this.calendarService.getTitle(portalControllerContext);
        if (StringUtils.isNotBlank(title)) {
            response.setTitle(title);
        }

        // Portlet URI
        this.calendarService.definePortletUri(portalControllerContext);

        return this.calendarService.getViewPath(portalControllerContext, calendarData);
    }


    /**
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @param exception current exception
     * @return error path
     */
    @ExceptionHandler(value = Exception.class)
    public String handleException(PortletRequest request, PortletResponse response, Exception exception) {
        // Error message
        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());

        return ERROR_PATH;
    }


    /**
     * Select today action mapping.
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "today")
    public void selectToday(ActionRequest request, ActionResponse response) throws PortletException {
        // Reset selected date : default date is today
        response.setRenderParameter(ICalendarService.SELECTED_DATE_PARAMETER, StringUtils.EMPTY);
    }


    /**
     * Select previous period action mapping.
     *
     * @param request action request
     * @param response action response
     * @param calendarData calendar data
     * @throws PortletException
     */
    @ActionMapping(value = "previous")
    public void selectPreviousPeriod(ActionRequest request, ActionResponse response, @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Selected date
        String selectedDate = this.calendarService.selectPreviousPeriod(portalControllerContext, calendarData);
        response.setRenderParameter(ICalendarService.SELECTED_DATE_PARAMETER, selectedDate);
    }


    /**
     * Select next period action mapping.
     *
     * @param request action request
     * @param response action response
     * @param calendarData calendar data
     * @throws PortletException
     */
    @ActionMapping(value = "next")
    public void selectNextPeriod(ActionRequest request, ActionResponse response, @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Selected date
        String selectedDate = this.calendarService.selectNextPeriod(portalControllerContext, calendarData);
        response.setRenderParameter(ICalendarService.SELECTED_DATE_PARAMETER, selectedDate);
    }


    /**
     * Get calendar data.
     *
     * @param request portlet request
     * @param response portlet response
     * @param periodTypeName period type name request parameter, may be null
     * @return calendar data
     * @throws PortletException
     */
    @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE)
    public CalendarData getCalendarData(PortletRequest request, PortletResponse response, @RequestParam(value = ICalendarService.PERIOD_TYPE_PARAMETER,
            required = false) String periodTypeName) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.calendarService.getCalendarData(portalControllerContext, periodTypeName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
