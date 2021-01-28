package org.osivia.services.editor.image.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.osivia.services.editor.image.portlet.model.AttachedImage;
import org.osivia.services.editor.image.portlet.model.SearchScope;

import javax.portlet.PortletException;
import java.io.File;
import java.util.List;
import java.util.SortedSet;

/**
 * Editor image portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface EditorImageRepository extends CommonRepository {

    /**
     * WebId document property.
     */
    String WEB_ID_PROPERTY = "ttc:webid";
    /**
     * Attached images document property.
     */
    String ATTACHED_IMAGES_PROPERTY = "ttc:images";

    /**
     * Attached image URL prefix.
     */
    String ATTACHED_IMAGE_URL_PREFIX = "/nuxeo/nxfile/default/attachedImages/";
    /**
     * Nuxeo document URL prefix.
     */
    String DOCUMENT_URL_PREFIX = "/nuxeo/web/";
    /**
     * Nuxeo document URL suffix.
     */
    String DOCUMENT_URL_SUFFIX = "?content=Original";


    /**
     * Get attached images.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return attached images
     */
    SortedSet<AttachedImage> getAttachedImages(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Add attached image.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param temporaryFile           attached image temporary file
     * @param fileName                attached image file name
     * @param contentType             attached image content type
     */
    void addAttachedImage(PortalControllerContext portalControllerContext, String path, File temporaryFile, String fileName, String contentType) throws PortletException;


    /**
     * Get attached image URL.
     *
     * @param portalControllerContext portal controller context
     * @param attachedImage           attached image
     * @return URL
     */
    String getAttachedImageUrl(PortalControllerContext portalControllerContext, AttachedImage attachedImage) throws PortletException;


    /**
     * Delete attached image.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param index                   attached image index
     */
    void deleteAttachedImage(PortalControllerContext portalControllerContext, String path, int index) throws PortletException;


    /**
     * Search image documents.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param filter                  search filter
     * @param scope                   search scope
     * @return documents
     */
    List<Document> search(PortalControllerContext portalControllerContext, String basePath, String filter, SearchScope scope) throws PortletException;


    /**
     * Get image document URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return URL
     */
    String getImageDocumentUrl(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get image document preview binary content.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   image webId
     * @param content                 image content
     * @return binary content
     */
    CMSBinaryContent getImageDocumentPreviewBinaryContent(PortalControllerContext portalControllerContext, String webId, String content) throws PortletException;


    /**
     * Get attached image preview binary content.
     *
     * @param portalControllerContext portal controller context
     * @param index                   attached image index
     * @return binary content
     */
    CMSBinaryContent getAttachedImagePreviewBinaryContent(PortalControllerContext portalControllerContext, int index) throws PortletException;

}
