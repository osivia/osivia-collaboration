package org.osivia.services.calendar.view.portlet.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.repository.CalendarRepository;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.view.portlet.model.CalendarOptions;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventKey;
import org.osivia.services.calendar.view.portlet.model.events.EventToSync;

/**
 * Calendar repository interface.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see CalendarRepository
 */
public interface CalendarViewRepository extends CalendarRepository {

    //Constants of VEVENT
    /** Calendar event document type name. */
    String DOCUMENT_TYPE_EVENEMENT = "VEVENT";
    /** Start date Nuxeo property. */
    String START_DATE_PROPERTY = "vevent:dtstart";
    /** End date Nuxeo property. */
    String END_DATE_PROPERTY = "vevent:dtend";
    /** All day Nuxeo property. */
    String ALL_DAY_PROPERTY = "vevent:allDay";
    /** Background color */
    String BCKG_COLOR = "vevent:color";
    /** Title */
    String TITLE_PROPERTY = "dc:title";
    /** Description */
    String DESCRIPTION_PROPERTY = "dc:description";
    /** Id event source */
    String ID_SOURCE_PROPERTY = "sync:idSource";
    /** Id parent source */
    String ID_PARENT_SOURCE_PROPERTY = "sync:idParentSource";
    /** Creation date source */
    String CREATED_SOURCE = "sync:created";
    /** Last modified date source */
    String LAST_MODIFIED_SOURCE = "sync:lastModified";
    /** Start date of the recurring source event */
    String START_DATE_RECCURING_SOURCE = "sync:startDateReccuringSource";
    
    //Constants of Agenda
    /** Calendar document type name. */
    String DOCUMENT_TYPE_AGENDA = "Agenda";
    /** URL to synchronize agenda */
    String URL_SYNCHRONIZATION = "url";
    /** Source ID */
    String SOURCEID_SYNCHRONIZATION = "sourceId";
    /** Default color of synchronized events */
    String COLOR_SYNCHRONIZATION = "color";
    /** Default color of synchronized events */
    String DISPLAYNAME_SYNCHRONIZATION = "displayName";
    /** List of synchronization sources */
    String LIST_SOURCE_SYNCHRO = "cal:sources";
    /** Color of the primary calendar */
    String PRIMARY_CALENDAR_COLOR = "cal:color";


    /**
     * Get calendar configuration.
     *
     * @param portalControllerContext portal controller context
     * @return calendar configuration
     * @throws PortletException
     */
    CalendarOptions getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save calendar configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration calendar configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, CalendarOptions configuration) throws PortletException;


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
     * @param startDate
     * @param endDate
     * @return calendar events
     * @throws PortletException
     */
    List<Event> getEvents(PortalControllerContext portalControllerContext, Date startDate, Date endDate) throws PortletException;

    /**
     * Get calendar event
     * 
     * @param portalControllerContext
     * @param docid
     * @return
     * @throws PortletException
     */
    Event getEvent(PortalControllerContext portalControllerContext, String docid) throws PortletException;
    
    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException;

    /**
     * Remove document using document_id
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void remove(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException;
    
    /**
     * Define portlet URI.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * To know if the event is editable
     * @param portalControllerContext
     * @param docid
     * @return
     * @throws PortletException
     */
    boolean isEventEditable(PortalControllerContext portalControllerContext, String docid) throws PortletException;
    
    /**
     * Synchronization of events
     * @param portalControllerContext
     * @param map map of events
     * @return true if synchronization success
     * @throws PortletException
     */
    void synchronize(PortalControllerContext portalControllerContext, Map<EventKey, EventToSync> map) throws PortletException;
    
    /**
     * Return the list of the synchronization sources of the current agenda
     * 
     * @param portalControllerContext
     * @return list of URLs
     * @throws PortletException
     */
    List<CalendarSynchronizationSource> getSynchronizationSources(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Return the colorId of the agenda
     * @param portalControllerContext
     * @return
     * @throws PortletException
     */
    String getColorIdAgenda(PortalControllerContext portalControllerContext) throws PortletException;

}
