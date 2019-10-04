package org.osivia.services.calendar.integration.portlet.service;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.integration.portlet.model.CalendarIntegrationForm;

import javax.portlet.PortletException;

/**
 * Calendar integration portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CalendarIntegrationService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-calendar-integration-instance";

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = Constants.WINDOW_PROP_URI;


    /**
     * Get calendar integration form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    CalendarIntegrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

}
