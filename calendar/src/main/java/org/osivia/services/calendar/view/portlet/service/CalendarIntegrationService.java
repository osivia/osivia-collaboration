package org.osivia.services.calendar.view.portlet.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Calendar integration portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CalendarIntegrationService {

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

}
