package org.osivia.services.calendar.event.edition.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.service.CalendarService;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;

/**
 * Calendar event edition portlet service.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarService
 */
public interface CalendarEventEditionService extends CalendarService {

    /** Date format pattern. */
    String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    /** Time format pattern. */
    String TIME_FORMAT_PATTERN = "HH:mm";


    /**
     * Get calendar event edition form.
     *
     * @param portalControllerContext portal controller context
     * @return calendar event edition form
     * @throws PortletException
     */
    CalendarEventEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Upload attachments.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadAttachments(PortalControllerContext portalControllerContext, CalendarEventEditionForm form) throws PortletException, IOException;


    /**
     * Delete attachment.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @param index attachment index
     * @throws PortletException
     * @throws IOException
     */
    void deleteAttachment(PortalControllerContext portalControllerContext, CalendarEventEditionForm form, int index) throws PortletException, IOException;


    /**
     * Restore attachment.
     * 
     * @param portalControllerContext portal controller context
     * @param form calendar event edition form
     * @param index attachment index
     * @throws PortletException
     * @throws IOException
     */
    void restoreAttachment(PortalControllerContext portalControllerContext, CalendarEventEditionForm form, int index) throws PortletException, IOException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @param form calendar event edition form
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEventEditionForm form)
            throws PortletException, IOException;


    /**
     * Cancel.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     * @throws IOException
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;

}
