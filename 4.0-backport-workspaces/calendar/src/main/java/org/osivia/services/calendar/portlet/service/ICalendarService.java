package org.osivia.services.calendar.portlet.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.EventsData;

/**
 * Calendar service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ICalendarService {

    /** Selected date format. */
    DateFormat SELECTED_DATE_FORMAT = SimpleDateFormat.getDateInstance(DateFormat.SHORT);

    /** Period type render parameter name. */
    String PERIOD_TYPE_PARAMETER = "period";

    /** Selected date render parameter name. */
    String SELECTED_DATE_PARAMETER = "date";


    /**
     * Get calendar title.
     *
     * @param portalControllerContext portal controller context
     * @return title
     * @throws PortletException
     */
    String getTitle(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get calendar data.
     *
     * @param portalControllerContext portal controller context
     * @param periodTypeName period type name, may be null
     * @return calendar data
     * @throws PortletException
     */
    CalendarData getCalendarData(PortalControllerContext portalControllerContext, String periodTypeName) throws PortletException;


    /**
     * Check if current calendar is compact.
     *
     * @param portalControllerContext portal controller context
     * @return true if compact
     * @throws PortletException
     */
    boolean isCompact(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get events data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return calendar events
     * @throws PortletException
     */
    EventsData getEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * Get view path.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return view path
     * @throws PortletException
     */
    String getViewPath(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * Select previous period.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return selected date
     * @throws PortletException
     */
    String selectPreviousPeriod(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * Select next period.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return selected date
     * @throws PortletException
     */
    String selectNextPeriod(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * Define portlet URI.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException;

}
