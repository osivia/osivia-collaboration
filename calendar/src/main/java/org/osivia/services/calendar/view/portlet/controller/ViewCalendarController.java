package org.osivia.services.calendar.view.portlet.controller;

import static org.osivia.services.calendar.view.portlet.service.CalendarViewService.DATE_PARAMETER;
import static org.osivia.services.calendar.view.portlet.service.CalendarViewService.PERIOD_TYPE_PARAMETER;
import static org.osivia.services.calendar.view.portlet.service.CalendarViewService.SELECTED_DATE_FORMAT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.view.portlet.model.CalendarEditionMode;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.service.CalendarViewService;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import net.sf.json.JSONArray;

/**
 * View calendar controller.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewCalendarController {

    /** Error JSP path. */
    private static final String ERROR_PATH = "error";
    /** Calendar data request attribute. */
    protected static final String CALENDAR_DATA_ATTRIBUTE = "calendarData";
    /** Events data request attribute. */
    private static final String EVENTS_DATA_ATTRIBUTE = "eventsData";
    /** Error message request attribute. */
    private static final String ERROR_MESSAGE_ATTRIBUTE = "message";
    /** Day for view day */
    private static final String DAY_FOR_VIEW_DAY = "dayForViewDay";
    /** Day for view week */
    private static final String DAY_FOR_VIEW_WEEK = "dayForViewWeek";
    /** Day for view month */
    private static final String DAY_FOR_VIEW_MONTH = "dayForViewMonth";
    /** Parameter for the scroll position in view day or view week */
    private static final String SCROLL_DAY_WEEK_PARAMETER = "scrollViewDayWeek";
    /** Parameter for the scroll position in view month */
    private static final String SCROLL_MONTH_PARAMETER = "scrollViewMonth";


    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;

    /** Calendar service. */
    @Autowired
    protected CalendarViewService calendarService;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory urlFactory;

    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public ViewCalendarController() {
        super();
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
    public String view(RenderRequest request, RenderResponse response, @RequestParam(value = DATE_PARAMETER, required = false) String startDate,
            @RequestParam(value = PERIOD_TYPE_PARAMETER, required = false) String periodType,
            @RequestParam(value = SCROLL_DAY_WEEK_PARAMETER, required = false) String scrollViewDayWeek,
            @RequestParam(value = SCROLL_MONTH_PARAMETER, required = false) String scrollViewMonth, @ModelAttribute CalendarData calendarData)
            throws PortletException {
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (null != periodType)
            calendarData.setPeriodType(PeriodTypes.fromName(periodType));
        if (null != startDate) {
            try {
                calendarData.setStartDate(SELECTED_DATE_FORMAT.parse(startDate));
            } catch (ParseException e) {
                // Parsing error, do nothing
            }
        }

        // Data are loaded with initSchedulerData or with loadData if periodType is not "planning"
        if (PeriodTypes.PLANNING.getName().equals(calendarData.getPeriodType().getName())) {
            // Events data
            request.setAttribute(EVENTS_DATA_ATTRIBUTE, this.calendarService.getEventsData(portalControllerContext, calendarData));
        }

        // Title
        String title = this.calendarService.getTitle(portalControllerContext);
        if (StringUtils.isNotBlank(title))
            response.setTitle(title);

        // Management of the Day and Week buttons' behavior
        Calendar calendar = Calendar.getInstance();
        int currentMonthOfYear = calendar.get(Calendar.MONTH);
        int currentWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(calendarData.getStartDate());
        int startDateMonthOfYear = calendar.get(Calendar.MONTH);
        int startDateWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        Calendar calFirstDayOfWeek = Calendar.getInstance();
        calFirstDayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date firstDayOfCurrentWeek = calFirstDayOfWeek.getTime();
        calFirstDayOfWeek.setTime(calendarData.getStartDate());
        calFirstDayOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date firstDayOfWeek = calFirstDayOfWeek.getTime();

        calendar.setTime(calendarData.getStartDate());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();

        Date dayForViewDay = null;
        Date dayForViewWeek = null;
        Date dayForViewMonth = null;
        Date today = Calendar.getInstance().getTime();
        if (PeriodTypes.MONTH.getName().equals(calendarData.getPeriodType().getName())) {
            // If it's the current month
            // - Day button return the current day
            // - Week button return the current week
            // Else, return the 1st day of month and the 1st week of month
            if (currentMonthOfYear == startDateMonthOfYear) {
                dayForViewDay = today;
                dayForViewWeek = firstDayOfCurrentWeek;
            } else {
                dayForViewDay = firstDayOfMonth;
                dayForViewWeek = firstDayOfMonth;
            }
            dayForViewMonth = firstDayOfMonth;
        } else if (PeriodTypes.WEEK.getName().equals(calendarData.getPeriodType().getName())) {
            // If it's the current week, Day button return the current day
            // Else, return the 1st day of the week
            if (currentWeekOfYear == startDateWeekOfYear) {
                dayForViewDay = today;
            } else {
                dayForViewDay = firstDayOfWeek;
            }

            dayForViewWeek = calendarData.getStartDate();
            dayForViewMonth = firstDayOfMonth;
        } else {
            dayForViewDay = calendarData.getStartDate();
            dayForViewWeek = firstDayOfWeek;
            // If we are on workspace but not yet in the agenda, endDate is not fulfilled
            if (calendarData.getEndDate() != null) {
                calendar.setTime(calendarData.getEndDate());
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                dayForViewMonth = calendar.getTime();
            } else {
                dayForViewMonth = dayForViewWeek;
            }
        }

        // Scroll position's management
        if (scrollViewDayWeek != null) {
            request.getPortletSession().setAttribute(SCROLL_DAY_WEEK_PARAMETER, scrollViewDayWeek);
            calendarData.setScrollViewDayWeek(scrollViewDayWeek);
        } else {
            if (request.getPortletSession().getAttribute(SCROLL_DAY_WEEK_PARAMETER) == null) {
                calendarData.setScrollViewDayWeek("-1");
            } else {
                calendarData.setScrollViewDayWeek((String) request.getPortletSession().getAttribute(SCROLL_DAY_WEEK_PARAMETER));
            }
        }
        if (scrollViewMonth != null) {
            request.getPortletSession().setAttribute(SCROLL_MONTH_PARAMETER, scrollViewMonth);
            calendarData.setScrollViewMonth(scrollViewMonth);
        } else {
            if (request.getPortletSession().getAttribute(SCROLL_MONTH_PARAMETER) == null) {
                calendarData.setScrollViewMonth("-1");
            } else {
                calendarData.setScrollViewMonth((String) request.getPortletSession().getAttribute(SCROLL_MONTH_PARAMETER));
            }
        }

        request.setAttribute(DAY_FOR_VIEW_DAY, dayForViewDay);
        request.setAttribute(DAY_FOR_VIEW_WEEK, dayForViewWeek);
        request.setAttribute(DAY_FOR_VIEW_MONTH, dayForViewMonth);
        request.setAttribute(CALENDAR_DATA_ATTRIBUTE, calendarData);

        // Set agenda color for quick creation event
        String colorIdAgenda = this.calendarService.getColorIdAgenda(portalControllerContext);
        calendarData.setAgendaBackgroundColor(CalendarColor.fromId(colorIdAgenda).getBackgroundClass());

        // Read only indicator
        boolean readOnly = this.calendarService.isCalendarReadOnly(portalControllerContext);
        calendarData.setReadOnly(readOnly);

        // Portlet URI
        this.calendarService.definePortletUri(portalControllerContext);

        return this.calendarService.getViewPath(portalControllerContext, calendarData);
    }


    /**
     * Ajax call to load scheduler events
     *
     * @param request resource request
     * @param response resource response
     * @param path parent path, may be null for root node
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "initSchedulerData")
    public void initSchedulerData(ResourceRequest request, ResourceResponse response,
            @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData,
            @RequestParam(value = PERIOD_TYPE_PARAMETER, required = false) String periodTypeName,
            @RequestParam(value = DATE_PARAMETER, required = false) Date selectedDate) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        dataLoading(response, portalControllerContext, calendarData);

    }


    /**
     * Events loading between start date and end date parameters
     *
     * @param request resource request
     * @param response resource response
     * @param start start date request parameter
     * @param end end date request parameter
     * @param calendarData calendar data model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "loadData")
    public void loadData(ResourceRequest request, ResourceResponse response, @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end, @ModelAttribute(CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Start date
        Date startDate;
        if (StringUtils.isEmpty(start)) {
            startDate = new Date();
        } else {
            startDate = new Date(Long.valueOf(start));
        }
        calendarData.setStartDate(startDate);

        // End date
        Date endDate;
        if (StringUtils.isEmpty(end)) {
            endDate = null;
        } else {
            endDate = new Date(Long.valueOf(Long.valueOf(end)));
        }
        calendarData.setEndDate(endDate);

        dataLoading(response, portalControllerContext, calendarData);
    }


    @ResourceMapping(value = "isEventEditable")
    public void isEventEditable(ResourceRequest request, ResourceResponse response, @RequestParam(value = "id", required = true) String id)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        Boolean isEventEditable = new Boolean(this.calendarService.isEventEditable(portalControllerContext, id));
        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(isEventEditable.toString());
        printWriter.close();
    }


    /**
     * Action called to view event's details
     *
     * @param request resource request
     * @param response resource response
     * @param path parent path, may be null for root node
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(value = "viewEvent")
    public void viewEvent(ActionRequest request, ActionResponse response, PortletSession session, @RequestParam("doc_id") String docid,
            @RequestParam(value = SCROLL_DAY_WEEK_PARAMETER, required = false) String scrollViewDayWeek,
            @RequestParam(value = SCROLL_MONTH_PARAMETER, required = false) String scrollViewMonth) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Storage of scroll position in session
        if (scrollViewDayWeek == null)
            scrollViewDayWeek = "-1";
        if (scrollViewMonth == null)
            scrollViewMonth = "-1";
        session.setAttribute(SCROLL_DAY_WEEK_PARAMETER, scrollViewDayWeek);
        session.setAttribute(SCROLL_MONTH_PARAMETER, scrollViewMonth);

        Event event = this.calendarService.getEvent(portalControllerContext, docid);
        ((ActionResponse) response).sendRedirect(event.getViewURL());
    }


    /**
     * Event'save after drag and drop
     * 
     * @param request
     * @param response
     * @param session
     * @param startDate
     * @param endDate
     * @param docid
     * @param title
     * @param scrollViewDayWeek
     * @param scrollViewMonth
     * @param period
     * @param date
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("dragndrop")
    public void dragndrop(ActionRequest request, ActionResponse response, PortletSession session, @RequestParam("start") String startDate,
            @RequestParam("end") String endDate, @RequestParam("doc_id") String docid, 
            @RequestParam("title") String title,
            @RequestParam(value = SCROLL_DAY_WEEK_PARAMETER, required = false) String scrollViewDayWeek,
            @RequestParam(value = SCROLL_MONTH_PARAMETER, required = false) String scrollViewMonth, @RequestParam(value = PERIOD_TYPE_PARAMETER) String period,
            @RequestParam(value = DATE_PARAMETER) String date) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CalendarViewForm form = new CalendarViewForm();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df1.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            form.setEndDate(df1.parse(endDate));
            form.setStartDate(df1.parse(startDate));

            form.setTitle(title);
            if ("undefined".equals(docid)) {
                form.setDocId(null);
                form.setMode(CalendarEditionMode.CREATION);
            } else {
                form.setDocId(docid);
                form.setMode(CalendarEditionMode.EDITION);
            }

            this.calendarService.save(portalControllerContext, form);
        } catch (ParseException e) {
            // Bundle
            Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_EVENT_NOT_SAVE"), NotificationsType.WARNING);
        }


        setResponseParameter(response, date, period, scrollViewDayWeek, scrollViewMonth);
    }


    /**
     * Event's removal
     * 
     * @param request
     * @param response
     * @param session
     * @param docid
     * @param scrollViewDayWeek
     * @param scrollViewMonth
     * @param period
     * @param date
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(value = "remove")
    public void remove(ActionRequest request, ActionResponse response, PortletSession session, @RequestParam("doc_id") String docid,
            @RequestParam(value = SCROLL_DAY_WEEK_PARAMETER, required = false) String scrollViewDayWeek,
            @RequestParam(value = SCROLL_MONTH_PARAMETER, required = false) String scrollViewMonth, @RequestParam(value = PERIOD_TYPE_PARAMETER) String period,
            @RequestParam(value = DATE_PARAMETER) String date) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        CalendarViewForm form = new CalendarViewForm();
        if (!"undefined".equals(docid)) {
            form.setDocId(docid);
            // Récupérer l'évènement concerné et modifier sa date de début et de fin
            this.calendarService.remove(portalControllerContext, form);
        }

        setResponseParameter(response, date, period, scrollViewDayWeek, scrollViewMonth);
    }


    /**
     * Synchronization
     * 
     * @param request
     * @param response
     * @param session
     * @param scrollViewDayWeek
     * @param scrollViewMonth
     * @param period
     * @param date
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(value = "synchronize")
    public void synchronize(ActionRequest request, ActionResponse response, PortletSession session) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.calendarService.synchronize(portalControllerContext);

        // Redirect to refresh the page
        response.sendRedirect(this.urlFactory.getRefreshPageUrl(portalControllerContext));
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
    public CalendarData getCalendarData(PortletRequest request, PortletResponse response,
            @RequestParam(value = PERIOD_TYPE_PARAMETER, required = false) String periodTypeName) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.calendarService.getCalendarData(portalControllerContext, periodTypeName);
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
     * Event's loading from calendarData's start date and end date
     * 
     * @param response
     * @param portalControllerContext
     * @param calendarData
     * @throws PortletException
     * @throws IOException
     */
    protected void dataLoading(ResourceResponse response, PortalControllerContext portalControllerContext, CalendarData calendarData)
            throws PortletException, IOException {
        JSONArray array = this.calendarService.loadEventsArray(portalControllerContext, calendarData);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(array.toString());
        printWriter.close();
    }


    protected void setResponseParameter(ActionResponse response, String date, String period, String scrollViewDayWeek, String scrollViewMonth) {
        response.setRenderParameter(DATE_PARAMETER, date);
        response.setRenderParameter(PERIOD_TYPE_PARAMETER, period);
        if (scrollViewDayWeek == null)
            scrollViewDayWeek = "-1";
        if (scrollViewMonth == null)
            scrollViewMonth = "-1";
        response.setRenderParameter(SCROLL_DAY_WEEK_PARAMETER, scrollViewDayWeek);
        response.setRenderParameter(SCROLL_MONTH_PARAMETER, scrollViewMonth);
    }

}
