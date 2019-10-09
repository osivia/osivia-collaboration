package org.osivia.services.calendar.common.service;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarOptions;

/**
 * Calendar service interface.
 * 
 * @author Frédéric Boudan
 */
public interface RssService {

    /** Calendar creation indicator window property. */
    String CREATION_PROPERTY = "osivia.calendar.creation";


    /**
     * Get calendar options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    CalendarOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get calendar edition options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    CalendarEditionOptions getEditionOptions(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Get calendar color
     * @param portalControllerContext
     * @param calendar
     * @return
     * @throws PortletException
     */
    CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, Document calendar) throws PortletException;
}
