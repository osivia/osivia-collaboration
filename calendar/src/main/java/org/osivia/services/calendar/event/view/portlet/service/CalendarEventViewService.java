package org.osivia.services.calendar.event.view.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.service.CalendarService;
import org.osivia.services.calendar.event.view.portlet.model.CalendarEventViewForm;

/**
 * Calendar event edition portlet service.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarService
 */
public interface CalendarEventViewService extends CalendarService {

    /** Date format pattern. */
    String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    /** Time format pattern. */
    String TIME_FORMAT_PATTERN = "HH:mm";
    
    /** Title Nuxeo document property. */
    String TITLE_PROPERTY = "dc:title";
    /** Start date Nuxeo document property. */
    String START_DATE_PROPERTY = "vevent:dtstart";
    /** End date Nuxeo document property. */
    String END_DATE_PROPERTY = "vevent:dtend";
    /** All day indicator Nuxeo property. */
    String ALL_DAY_PROPERTY = "vevent:allDay";
    /** Location Nuxeo document property. */
    String LOCATION_PROPERTY = "vevent:location";
    /** Color Nuxeo document property. */
    String COLOR_PROPERTY = "vevent:color";
    /** Description Nuxeo document property. */
    String DESCRIPTION_PROPERTY = "dc:description";
    /** Id parent source */
    String ID_PARENT_SOURCE_PROPERTY = "sync:idParentSource";
    
    //Agenda property
    /** List of synchronization sources */
    String LIST_SOURCE_SYNCHRO = "cal:sources";
    /** Calendar color Nuxeo document property. */
    String CALENDAR_COLOR_PROPERTY = "cal:color";
    /** Source ID */
    String SOURCEID_SYNCHRONIZATION = "sourceId";
    /** Default color of synchronized events */
    String COLOR_SYNCHRONIZATION = "color";


    /**
     * Get calendar event visualization form.
     *
     * @param portalControllerContext portal controller context
     * @return calendar event edition form
     * @throws PortletException
     */
    CalendarEventViewForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Insert content menubar items.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;
    
}
