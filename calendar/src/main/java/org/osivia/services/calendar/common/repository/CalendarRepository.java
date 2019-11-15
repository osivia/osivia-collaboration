package org.osivia.services.calendar.common.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

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
     * Insert content menubar items.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Check if document is in workspace.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @return workspace indicator
     */
    boolean isWorkspace(PortalControllerContext portalControllerContext, Document document) throws PortletException;

}
