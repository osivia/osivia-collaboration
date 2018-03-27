package org.osivia.services.calendar.common.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarColor;

/**
 * Calendar repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CalendarRepository {

    /** Title Nuxeo document property. */
    String TITLE_PROPERTY = "dc:title";
    /** Calendar color Nuxeo document property. */
    String CALENDAR_COLOR_PROPERTY = "cal:color";


    /**
     * Get current Nuxeo document.
     * 
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get title.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return title
     */
    String getTitle(PortalControllerContext portalControllerContext, Document document);


    /**
     * Get calendar color.
     * 
     * @param portalControllerContext portal controller context
     * @param calendar calendar Nuxeo document
     * @return color
     * @throws PortletException
     */
    CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, Document calendar) throws PortletException;
    
    /**
     * Insert content menubar items.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;

}
