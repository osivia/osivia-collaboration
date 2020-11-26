package org.osivia.services.editor.image.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.repository.CommonRepository;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Editor image portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface EditorImageRepository extends CommonRepository {

    /**
     * Search image documents.
     *
     * @param portalControllerContext portal controller
     * @param filter                  search filter
     * @return documents
     */
    List<Document> search(PortalControllerContext portalControllerContext, String filter) throws PortletException;


    /**
     * Get image document URL.
     *
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return URL
     */
    String getImageDocumentUrl(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
