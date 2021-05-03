package org.osivia.services.editor.link.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.repository.CommonRepository;

import javax.portlet.PortletException;

/**
 * Editor link portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface EditorLinkRepository extends CommonRepository {

    /**
     * Get document URL from webId.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   webId
     * @return URL
     */
    String getDocumentUrl(PortalControllerContext portalControllerContext, String webId) throws PortletException;


    /**
     * Get document from URL.
     *
     * @param portalControllerContext portal controller context
     * @param url                     URL
     * @return document, or null if URL isn't a Nuxeo URL, or if the document is not found
     */
    Document getDocumentFromUrl(PortalControllerContext portalControllerContext, String url) throws PortletException;

}
