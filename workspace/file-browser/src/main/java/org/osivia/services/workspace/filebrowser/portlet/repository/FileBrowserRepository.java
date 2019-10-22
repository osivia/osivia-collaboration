package org.osivia.services.workspace.filebrowser.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserWindowProperties;
import org.springframework.web.multipart.MultipartFile;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * File browser portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface FileBrowserRepository {

    /**
     * Get document base path.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return path
     */
    String getBasePath(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) throws PortletException;


    /**
     * Get content path.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return path
     */
    String getContentPath(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) throws PortletException;


    /**
     * Get document context.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document context
     */
    NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get documents.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @param parentPath              parent document path
     * @return documents
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties, String parentPath) throws PortletException;


    /**
     * Get publication infos.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @return publication infos
     */
    NuxeoPublicationInfos getPublicationInfos(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get permissions.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @return permissions
     */
    NuxeoPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get user subscriptions.
     *
     * @param portalControllerContext portal controller context
     * @return document identifiers
     */
    Set<String> getUserSubscriptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get parent Nuxeo document.
     * For performance reasons, the parent document is taken from navigation.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @return parent Nuxeo document
     */
    Document getParentDocument(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get download URL.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @return URL
     */
    String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Duplicate document.
     *
     * @param portalControllerContext portal controller context
     * @param sourcePath              source document path
     * @param targetPath              target document path
     */
    void duplicate(PortalControllerContext portalControllerContext, String sourcePath, String targetPath) throws PortletException;


    /**
     * Delete documents.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             document identifiers
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException;


    /**
     * Get binary content.
     *
     * @param portalControllerContext portal controller context
     * @param paths                   document paths
     * @return binary content
     */
    CMSBinaryContent getBinaryContent(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Move documents.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIdentifiers       source document identifiers
     * @param targetIdentifier        target document identifier
     */
    void move(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException;


    /**
     * Import files.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param upload                  upload multipart files
     */
    void importFiles(PortalControllerContext portalControllerContext, String path, List<MultipartFile> upload) throws PortletException, IOException;


    /**
     * Update menubar.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     */
    void updateMenubar(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get parent Nuxeo documents.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return Nuxeo documents
     */
    List<Document> getParentDocuments(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
