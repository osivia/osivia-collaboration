package org.osivia.services.calendar.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.CalendarConfiguration;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.Event;

/**
 * Calendar repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ICalendarRepository {

    /** Start date Nuxeo property. */
    String START_DATE_PROPERTY = "vevent:dtstart";
    /** End date Nuxeo property. */
    String END_DATE_PROPERTY = "vevent:dtend";
    /** All day Nuxeo property. */
    String ALL_DAY_PROPERTY = "vevent:allDay";


    /**
     * Get calendar configuration.
     *
     * @param portalControllerContext portal controller context
     * @return calendar configuration
     * @throws PortletException
     */
    CalendarConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save calendar configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration calendar configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, CalendarConfiguration configuration) throws PortletException;


    /**
     * Get calendar title.
     *
     * @param portalControllerContext portal controller context
     * @return title
     * @throws PortletException
     */
    String getTitle(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get calendar events.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return calendar events
     * @throws PortletException
     */
    List<Event> getEvents(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * Insert content menubar items.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Define portlet URI.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException;

}
