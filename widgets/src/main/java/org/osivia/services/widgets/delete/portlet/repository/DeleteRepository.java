package org.osivia.services.widgets.delete.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;
import java.util.List;
import java.util.Map;

/**
 * Delete portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DeleteRepository {

    /**
     * Get document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document
     */
    Document getDocument(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get documents.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             document identifiers
     * @return documents
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException;


    /**
     * Get children counts.
     *
     * @param portalControllerContext portal controller context
     * @param documents               documents
     * @return children counts, sorted by document
     */
    Map<Document, Integer> getChildrenCounts(PortalControllerContext portalControllerContext, List<Document> documents) throws PortletException;


    /**
     * Delete documents.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             document identifiers
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException;

}
