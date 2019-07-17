package org.osivia.services.calendar.event.preview.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.event.preview.portlet.model.CalendarEventPreviewForm;

import javax.portlet.PortletException;

/**
 * Calendar event preview portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CalendarEventPreviewService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-calendar-event-preview-instance";

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.event-preview.path";
    /**
     * Page identifier window property.
     */
    String PAGE_ID_WINDOW_PROPERTY = "osivia.event-preview.page-id";


    /**
     * Get calendar event preview form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    CalendarEventPreviewForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

}
