package org.osivia.services.editor.common.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.model.SearchScope;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Portlet common repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CommonRepository {

    /**
     * WebId Nuxeo document property.
     */
    String WEB_ID_PROPERTY = "ttc:webid";

    /**
     * Nuxeo document URL prefix.
     */
    String DOCUMENT_URL_PREFIX = "/nuxeo/web/";


    /**
     * Get document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document
     */
    Document getDocument(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Search documents.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param filter                  search filter
     * @param scope                   search scope
     * @return documents
     */
    List<Document> searchDocuments(PortalControllerContext portalControllerContext, String basePath, String filter, SearchScope scope);

}
