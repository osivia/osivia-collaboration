package org.osivia.services.calendar.view.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Calendar integration portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CalendarIntegrationService {

    /**
     * Get calendar integration.
     * 
     * @param portalControllerContext portal controller context
     * @param format calendar integration format
     * @return integration
     * @throws PortletException
     * @throws IOException
     */
    String getIntegration(PortalControllerContext portalControllerContext, String format) throws PortletException, IOException;

}
