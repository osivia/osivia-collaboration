package org.osivia.services.calendar.view.portlet.service;

import java.io.IOException;
import java.io.OutputStream;
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
import org.osivia.services.calendar.view.portlet.model.events.EventToSync;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;

import net.fortuna.ical4j.model.component.VEvent;
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
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException;

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
     *
     * @param portalControllerContext
     * @param docid
     * @return
     * @throws PortletException
     */
    boolean isEventEditable(PortalControllerContext portalControllerContext, String docid) throws PortletException;

    /**
     * Synchronization of events
     *
     * @param portalControllerContext
     * @throws PortletException
     */
    void synchronize(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Return the map of the different color (primary color of the calendar and color of each synchrnonization sources)
     *
     * @param portalControllerContext
     * @return
     * @throws PortletException
     */
    Map<String, CalendarColor> getSourcesColor(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Return the id of the color of the agenda
     *
     * @param portalControllerContext
     * @return
     * @throws PortletException
     */
    String getColorIdAgenda(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * Check if calendar is read only.
     *
     * @param portalControllerContext portal controller context
     * @return true if calendar is read only
     * @throws PortletException
     */
    boolean isCalendarReadOnly(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Load events array
     *
     * @param portalControllerContext
     * @param calendarData
     * @return
     * @throws PortletException
     */
    JSONArray loadEventsArray(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;

    /**
     * Build event
     *
     * @param vevent
     * @param idAgenda
     * @param timeZoneAllEvent
     * @return
     */
    EventToSync buildEvent(VEvent vevent, String idAgenda, net.fortuna.ical4j.model.TimeZone timeZoneAllEvent) throws PortletException;


    /**
     * Integrate calendar.
     *
     * @param portalControllerContext portal controller context
     * @param outputStream output steam
     * @param format calendar integration format
     * @throws PortletException
     * @throws IOException
     */
    void integrate(PortalControllerContext portalControllerContext, OutputStream outputStream, String format) throws PortletException, IOException;


    /**
     * Get integration URL.
     *
     * @param portalControllerContext portal controller context
     * @return URL
     */
    String getIntegrationUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
