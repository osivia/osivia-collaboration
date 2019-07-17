package org.osivia.services.calendar.event.preview.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.jboss.portal.core.model.portal.PortalObjectId;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.Link;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;

/**
 * Calendar event preview portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarEventPreviewRepository
 */
@Repository
public class CalendarEventPreviewRepositoryImpl implements CalendarEventPreviewRepository {

    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    @Override
    public String getViewUrl(PortalControllerContext portalControllerContext, Document document, PortalObjectId pageObjectId) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document link
        Link link = nuxeoController.getLink(document);

        return link.getUrl();
    }
}
