package org.osivia.services.calendar.event.preview.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.jboss.portal.core.model.portal.PortalObjectId;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Calendar event preview portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CalendarEventPreviewRepository {

    /**
     * Get event document context.
     *
     * @param portalControllerContext portal controller context
     * @param path                    event document path
     * @return document context
     */
    NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get event view URL.
     *
     * @param portalControllerContext portal controller context
     * @param document                event document
     * @param pageObjectId            page identifier
     * @return URL
     */
    String getViewUrl(PortalControllerContext portalControllerContext, Document document, PortalObjectId pageObjectId) throws PortletException;

}
