package org.osivia.services.workspace.filebrowser.portlet.repository;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;

/**
 * File browser portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserRepository {

    /**
     * Get current path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get base path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getBasePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get current document context.
     * 
     * @param portalControllerContext portal controller context
     * @return document context
     * @throws PortletException
     */
    NuxeoDocumentContext getCurrentDocumentContext(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get documents.
     * 
     * @param portalControllerContext portal controller context
     * @return documents
     * @throws PortletException
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get publication infos.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return publication infos
     * @throws PortletException
     */
    NuxeoPublicationInfos getPublicationInfos(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get permissions.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return permissions
     * @throws PortletException
     */
    NuxeoPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get user subscriptions.
     * 
     * @param portalControllerContext portal controller context
     * @return document identifiers
     * @throws PortletException
     */
    Set<String> getUserSubscriptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get user preferences.
     * 
     * @param portalControllerContext portal controller context
     * @return user preferences
     * @throws PortletException
     */
    UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get download URL.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return URL
     * @throws PortletException
     */
    String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Duplicate document.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @throws PortletException
     */
    void duplicate(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Delete documents.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers document identifiers
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException;


    /**
     * Get binary content.
     * 
     * @param portalControllerContext portal controller context
     * @param paths document paths
     * @return binary content
     * @throws PortletException
     * @throws IOException
     */
    CMSBinaryContent getBinaryContent(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Move documents.
     * 
     * @param portalControllerContext portal controller context
     * @param sourceIdentifiers source document identifiers
     * @param targetIdentifier target document identifier
     * @throws PortletException
     */
    void move(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException;


    /**
     * Import files.
     * 
     * @param portalControllerContext portal controller context
     * @param upload upload multipart files
     * @throws PortletException
     * @throws IOException
     */
    void importFiles(PortalControllerContext portalControllerContext, List<MultipartFile> upload) throws PortletException, IOException;


    /**
     * Update menubar.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;

}
