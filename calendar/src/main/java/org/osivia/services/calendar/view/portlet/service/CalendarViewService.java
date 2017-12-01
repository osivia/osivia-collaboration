package org.osivia.services.calendar.view.portlet.service;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.service.CalendarService;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;

import net.sf.json.JSONArray;

/**
 * Calendar service interface.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see CalendarService
 */
public interface CalendarViewService extends CalendarService {

    /** Document type window property. */
    public final static String DOCUMENT_TYPE_PROPERTY = "osivia.calendar.edition.documentType";
    /** Calendar edition mode identifier window property. */
    public final static String MODE_PROPERTY = "osivia.calendar.edition.mode";
    /** Selected date format. */
    public final static SimpleDateFormat SELECTED_DATE_FORMAT = new SimpleDateFormat("yyyy,MM,dd");

    /** Period type render parameter name. */
    public final static String PERIOD_TYPE_PARAMETER = "period";

    /** Date render parameter name. */
    public final static String DATE_PARAMETER = "date";


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
     * Get docid
     * 
     * @param portalControllerContext
     * @param docid
     * @return
     * @throws PortletException
     */
    Event getEvent(PortalControllerContext portalControllerContext, String docid) throws PortletException;
    
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
    
    /**
     * Save event
     * 
     * @param portalControllerContext
     * @param form
     * @param timezone client timezone
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, CalendarViewForm form, TimeZone timezone) throws PortletException;
    
    /**
     * Remove event
     * 
     * @param portalControllerContext
     * @param form
     * @param options
     * @throws PortletException
     */
    void remove(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException;
    
    /**
     * Return true if event is editable
     * @param portalControllerContext
     * @param docid
     * @return
     * @throws PortletException
     */
    public boolean isEventEditable(PortalControllerContext portalControllerContext, String docid) throws PortletException;
    
    /**
     * Synchronization of events
     * @param portalControllerContext
     * @throws PortletException
     */
    public void synchronize(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Return the map of the different color (primary color of the calendar and color of each synchrnonization sources)
     * @param portalControllerContext
     * @return
     * @throws PortletException
     */
    public Map<String, CalendarColor> getSourcesColor(PortalControllerContext portalControllerContext)
			throws PortletException;
    
    /**
     * Return the id of the color of the agenda
     * @param portalControllerContext
     * @return
     * @throws PortletException
     */
    public String getColorIdAgenda(PortalControllerContext portalControllerContext)
			throws PortletException;
    
    /**
     * Load events array
     * @param portalControllerContext
     * @param calendarData
     * @return
     * @throws PortletException
     */
    public JSONArray loadEventsArray(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;

}
