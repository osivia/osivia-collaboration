package org.osivia.services.sets.edition.portlet.repository;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.sets.edition.portlet.model.AddedDocument;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Sets edition repository
 * @author Julien Barberet
 *
 */
public interface SetsEditionRepository {
	
    /**
     * Get workspace
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException;
    
    /**
     * Get workspace DTO
     * 
     * @param workspace workspace
     * @return documentDTO
     * @throws PortletException
     */
    DocumentDTO getWorkspaceDTO(Document workspace) throws PortletException;
	
    /**
     * Get documents with webid equals to listwebid
     * @param portalControllerContext
     * @param workspaceUid
     * @param listwebid
     * @return
     */
    public List<AddedDocument> getDocumentsList(PortalControllerContext portalControllerContext, String workspaceUid, List<Object> listwebid);
    
    /**
     * Save workspace to update its list of pinned documents
     * @param portalControllerContext
     * @param workspace
     * @param stringSet
     */
    void save(PortalControllerContext portalControllerContext, Document workspace, List<String> listPinSet);
    
    /**
     * Search documents with criteria
     * @param portalControllerContext
     * @param filter
     * @param page
     * @return
     */
    PaginableDocuments search(PortalControllerContext portalControllerContext, String filter, int page);
    
    /**
     * Get document properties
     * @param portalControllerContext
     * @param document
     * @param alreadyPinned
     * @return
     * @throws PortletException
     */
    Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document, boolean alreadyPinned) throws PortletException;
    
    /**
     * Get document form its weid
     * @param portalControllerContext
     * @param workspacePath
     * @param webid
     * @return
     */
    AddedDocument getDocument(PortalControllerContext portalControllerContext, String workspacePath, String webid);
}
