package org.osivia.services.editor.link.portlet.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.FileMimeType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Editor link portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface EditorLinkRepository {

    /** Select2 results page size. */
    int SELECT2_RESULTS_PAGE_SIZE = 10;


    /**
     * Get document URL from webId.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   webId
     * @return URL
     * @throws PortletException
     */
    String getDocumentUrl(PortalControllerContext portalControllerContext, String webId) throws PortletException;


    /**
     * Get document DTO from webId.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   webId
     * @return document DTO
     * @throws PortletException
     */
    DocumentDTO getDocumentDto(PortalControllerContext portalControllerContext, String webId) throws PortletException;


    /**
     * Search Nuxeo documents.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                search base path
     * @param filter                  search filter
     * @param page                    search pagination page number
     * @return paginable Nuxeo documents
     * @throws PortletException
     */
    PaginableDocuments searchDocuments(PortalControllerContext portalControllerContext, String basePath, String filter, int page) throws PortletException;


    /**
     * Get document properties.
     *
     * @param document Nuxeo document
     * @return properties
     * @throws PortletException
     */
    Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Create editor link form.
     *
     * @param portalControllerContext portal controller context
     * @param url                     URL
     * @param text                    text
     * @param title                   title
     * @param onlyText                only text indicator
     * @return form
     * @throws PortletException
     */
    EditorLinkForm createForm(PortalControllerContext portalControllerContext, String url, String text, String title, boolean onlyText) throws PortletException;


    /**
     * Get document types.
     * 
     * @param portalControllerContext portal controller context
     * @return document types
     * @throws PortletException
     */
    Collection<DocumentType> getDocumentTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get file MIME types.
     * 
     * @param portalControllerContext portal controller context
     * @return file MIME types
     * @throws PortletException
     * @throws IOException
     */
    Map<String, FileMimeType> getFileMimeTypes(PortalControllerContext portalControllerContext) throws PortletException, IOException;

}
