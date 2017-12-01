package org.osivia.services.calendar.synchronization.edition.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.service.CalendarService;
import org.osivia.services.calendar.synchronization.edition.portlet.model.CalendarSynchronizationEditionForm;

/**
 * Calendar synchronization source edition portlet service interface.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarService
 */
public interface CalendarSynchronizationEditionService extends CalendarService {

    /** Portlet instance. */
    String PORTLET_INSTANCE = "osivia-services-calendar-synchronization-edition-instance";

    /** Calendar synchronization source identifier window property. */
    String SYNCHRONIZATION_SOURCE_ID = "osivia.calendar.synchronizationSource.id";
    /** Calendar synchronization source URL window property. */
    String SYNCHRONIZATION_SOURCE_URL = "osivia.calendar.synchronizationSource.url";
    /** Calendar synchronization source color window property. */
    String SYNCHRONIZATION_SOURCE_COLOR = "osivia.calendar.synchronizationSource.color";
    /** Calendar synchronization source display name window property. */
    String SYNCHRONIZATION_SOURCE_DISPLAY_NAME = "osivia.calendar.synchronizationSource.displayName";


    /**
     * Get synchronization source edition form.
     * 
     * @param portalControllerContext portal controller context
     * @return synchronization source edition form
     * @throws PortletException
     */
    CalendarSynchronizationEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Submit synchronization source edition form.
     * 
     * @param portalControllerContext portal controller context
     * @param form synchronization source edition form
     * @throws PortletException
     */
    void submit(PortalControllerContext portalControllerContext, CalendarSynchronizationEditionForm form) throws PortletException;

}
