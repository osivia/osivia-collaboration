package org.osivia.services.calendar.event.edition.portlet.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.Attachments;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarEventDates;
import org.osivia.services.calendar.common.repository.CalendarRepository;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;

/**
 * Calendar repository interface.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 */
public interface CalendarEventEditionRepository extends CalendarRepository {

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
    String DESCRIPTION_PROPERTY = "note:note";
    /** Attachments Nuxeo document property. */
    String ATTACHMENTS_PROPERTY = "files:files";


    /**
     * Get calendar color.
     * 
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @return color
     * @throws PortletException
     */
    CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, CalendarEditionOptions options) throws PortletException;


    /**
     * Get all day indicator.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return all day indicator
     * @throws PortletException
     */
    boolean isAllDay(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get calendar event dates.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @param allDay all day indicator
     * @return dates
     * @throws PortletException
     */
    CalendarEventDates getDates(PortalControllerContext portalControllerContext, Document document, boolean allDay) throws PortletException;


    /**
     * Get calendar event location.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return location
     * @throws PortletException
     */
    String getLocation(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get calendar event color.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @param calendarColor calendar color
     * @return color
     * @throws PortletException
     */
    CalendarColor getColor(PortalControllerContext portalControllerContext, Document document, CalendarColor calendarColor) throws PortletException;


    /**
     * Get calendar event description.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return description
     * @throws PortletException
     */
    String getDescription(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get calendar event attachments.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo doucment
     * @return attachments
     * @throws PortletException
     */
    Attachments getAttachments(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @param form calendar event edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEventEditionForm form) throws PortletException;

}
