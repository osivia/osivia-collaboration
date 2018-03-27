package org.osivia.services.calendar.view.portlet.service.generator;

import java.util.Date;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;

/**
 * Calendar generator interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ICalendarGenerator {

    /** Number of days in a week. */
    int DAYS_IN_WEEK = 7;


    /**
     * Get generator period type.
     *
     * @return period type
     * @throws PortletException
     */
    PeriodTypes getPeriodType() throws PortletException;
    
    /**
     * Set generator period type.
     *
     * @throws PortletException
     */
    void setPeriodType(PeriodTypes periodType) throws PortletException;


    /**
     * Generate calendar data.
     *
     * @param portalControllerContext portal controller context
     * @return calendar data
     * @throws PortletException
     */
    CalendarData generateCalendarData(PortalControllerContext portalControllerContext, PeriodTypes periodType) throws PortletException;


    /**
     * Update calendar data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param selectedDate selected date, may be null
     * @throws PortletException
     */
    void updateCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData, Date selectedDate) throws PortletException;


    /**
     * Generate events data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return events data
     * @throws PortletException
     */
    EventsData generateEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;

}
