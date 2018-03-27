package org.osivia.services.calendar.common.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarOptions;

/**
 * Calendar service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CalendarService {

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
    
}
