package org.osivia.services.editor.image.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.osivia.services.editor.image.portlet.model.AttachedImage;

import javax.portlet.PortletException;
import java.io.File;
import java.io.IOException;
import java.util.SortedSet;

/**
 * Editor image portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface EditorImageRepository extends CommonRepository {

    /**
     * Attached images document property.
     */
    String ATTACHED_IMAGES_PROPERTY = "ttc:images";

    /**
     * Attached image URL prefix.
     */
    String ATTACHED_IMAGE_URL_PREFIX = "/nuxeo/nxfile/default/attachedImages/";


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
     * @param index                   attached image index
     * @param fileName                attached image file name
     * @return URL
     */
    String getAttachedImageUrl(PortalControllerContext portalControllerContext, int index, String fileName) throws PortletException, IOException;


    /**
     * Delete attached image.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param index                   attached image index
     */
    void deleteAttachedImage(PortalControllerContext portalControllerContext, String path, int index) throws PortletException;


    /**
     * Copy attached image.
     *
     * @param portalControllerContext portal controller context
     * @param sourcePath              source document path
     * @param targetPath              target document path
     */
    void copyAttachedImage(PortalControllerContext portalControllerContext, String sourcePath, String targetPath) throws PortletException;


    /**
     * Get source preview binary content.
     *
     * @param portalControllerContext portal controller context
     * @param path                    source path
     * @return binary content
     */
    CMSBinaryContent getSourcePreviewBinaryContent(PortalControllerContext portalControllerContext, String path) throws PortletException, IOException;


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


    /**
     * Get current path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException;

}
