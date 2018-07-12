package org.osivia.services.calendar.event.edition.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.converter.CalendarColorPropertyEditor;
import org.osivia.services.calendar.common.service.CalendarService;

/**
 * Calendar event edition portlet service.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarService
 */
public interface CalendarEventEditionService extends CalendarService {

    /** Date format pattern. */
	public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    /** Time format pattern. */
    public static final String TIME_FORMAT_PATTERN = "HH:mm";
    /** Color Nuxeo document property. */
    public static final String COLOR_PROPERTY = "vevent:color";


    /**
     * Get calendar event edition form.
     *
     * @param portalControllerContext portal controller context
     * @return calendar event edition form
     * @throws PortletException
     */
    CalendarCommonEventForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Upload attachments.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadAttachments(PortalControllerContext portalControllerContext, CalendarCommonEventForm form) throws PortletException, IOException;


    /**
     * Delete attachment.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @param index attachment index
     * @throws PortletException
     * @throws IOException
     */
    void deleteAttachment(PortalControllerContext portalControllerContext, CalendarCommonEventForm form, int index) throws PortletException, IOException;


    /**
     * Restore attachment.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @param index attachment index
     * @throws PortletException
     * @throws IOException
     */
    void restoreAttachment(PortalControllerContext portalControllerContext, CalendarCommonEventForm form, int index) throws PortletException, IOException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @param form calendar event edition form
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarCommonEventForm form)
            throws PortletException, IOException;


    /**
     * Cancel.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     * @throws IOException
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;
    
    /**
     * Get calendar color property editor
     * @return
     */
    CalendarColorPropertyEditor getCalendarColorPropertyEditor();

}
