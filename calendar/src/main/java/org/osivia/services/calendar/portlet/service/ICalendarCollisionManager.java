package org.osivia.services.calendar.portlet.service;

import java.util.Date;
import java.util.List;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;

/**
 * Calendar collision manager interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ICalendarCollisionManager {

    /**
     * Update daily events to avoid collisions.
     *
     * @param portalControllerContext portal controller context
     * @param events events
     * @param date current date
     */
    void updateDailyEvents(PortalControllerContext portalControllerContext, List<DailyEvent> events, Date date);


    /**
     * Update weekly events to avoid collisions.
     *
     * @param portalControllerContext portal controller context
     * @param events events
     * @param date current date
     */
    void updateWeeklyEvents(PortalControllerContext portalControllerContext, List<DailyEvent> events, Date date);

}
