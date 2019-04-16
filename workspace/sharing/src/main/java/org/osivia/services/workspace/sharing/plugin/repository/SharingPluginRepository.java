package org.osivia.services.workspace.sharing.plugin.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.common.repository.SharingCommonRepository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Sharing plugin repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingPluginRepository extends SharingCommonRepository {

    /**
     * Get sharing root Nuxeo document, or null if there is no sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param documentContext Nuxeo document context
     * @return Nuxeo document
     */
    Document getSharingRoot(PortalControllerContext portalControllerContext, NuxeoDocumentContext documentContext);


    /**
     * Get sharing author.
     * 
     * @param portalControllerContext portal controller context
     * @param sharingRoot sharing root Nuxeo document
     * @return author
     */
    String getSharingAuthor(PortalControllerContext portalControllerContext, Document sharingRoot);


    /**
     * Check if path is in current user workspace.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return true if path is in current user workspace
     * @throws PortalException
     */
    boolean isInCurrentUserWorkspace(PortalControllerContext portalControllerContext, String path) throws PortalException;

}
