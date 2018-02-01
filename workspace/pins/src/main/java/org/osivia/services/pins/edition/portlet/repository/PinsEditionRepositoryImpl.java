package org.osivia.services.pins.edition.portlet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.pins.edition.portlet.model.PinnedDocument;
import org.osivia.services.pins.edition.portlet.repository.command.GetDocumentCommand;
import org.osivia.services.pins.edition.portlet.repository.command.ListDocumentsCommand;
import org.osivia.services.pins.edition.portlet.repository.command.SaveWorkspaceCommand;
import org.osivia.services.pins.edition.portlet.repository.command.SearchDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Pins edition repository implementation
 * @author jbarberet
 *
 */
@Repository
public class PinsEditionRepositoryImpl implements PinsEditionRepository{

    /** Current workspace attribute name. */
    private static final String CURRENT_WORKSPACE_ATTRIBUTE = "osivia.workspace.edition.currentWorkspace";
    
    /** WebId Nuxeo document property. */
    private static final String WEB_ID_PROPERTY = "ttc:webid";
    /** Vignette Nuxeo document property. */
    private static final String VIGNETTE_PROPERTY = "ttc:vignette";
    /** Description Nuxeo document property. */
    private static final String DESCRIPTION_PROPERTY = "dc:description";
    
    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDAO;
	
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        
        // Workspace Nuxeo document
        Document workspace = (Document) request.getAttribute(CURRENT_WORKSPACE_ATTRIBUTE);
        
        if (workspace == null) {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // CMS base path
            String basePath = nuxeoController.getBasePath();
            if (basePath == null) {
                return null;
            } else
            {
	            // Nuxeo document context
	            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath);
	            documentContext.reload();
	
	            // Nuxeo document
	            workspace = documentContext.getDocument();
	
	            request.setAttribute(CURRENT_WORKSPACE_ATTRIBUTE, workspace);
            }
        }

        return workspace;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getWorkspaceDTO(Document workspace)
    {
    	if (workspace != null)
    	{
    		DocumentDTO workspaceDTO = documentDAO.toDTO(workspace);
    		return workspaceDTO;
    	}
    	return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<PinnedDocument> getDocumentsList(PortalControllerContext portalControllerContext, String workspacePath, List<Object> listwebid)
    {
    	 NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                 portalControllerContext.getPortletCtx());

         // Nuxeo command
         INuxeoCommand nuxeoCommand = new ListDocumentsCommand(NuxeoQueryFilterContext.CONTEXT_LIVE, listwebid, workspacePath, true);
         Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);    
         
         List<PinnedDocument> listPinned = new ArrayList<>();
         PinnedDocument pinnedDoc;
         Document doc;
         if (documents != null)
         {
        	 for (int i=0; i< documents.size(); i++)
        	 {
        		 doc = documents.get(i);
        		 pinnedDoc = new PinnedDocument(documentDAO.toDTO(doc), doc.getString(WEB_ID_PROPERTY), "deleted".equals(doc.getState()), i);
                 listPinned.add(pinnedDoc);
        	 }
         }
         
         return listPinned;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, Document workspace, PropertyMap propertyMap)
    {
    	NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());
    	
        // Nuxeo command
        INuxeoCommand nuxeoCommand = new SaveWorkspaceCommand(workspace, propertyMap);
        nuxeoController.executeNuxeoCommand(nuxeoCommand); 
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PaginableDocuments search(PortalControllerContext portalControllerContext, String criteria, int page)
    {
    	NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        // CMS base path
        String basePath = nuxeoController.getBasePath();
    	
        if (basePath != null)
        {
	        // Nuxeo command
	        INuxeoCommand nuxeoCommand = new SearchDocumentsCommand(basePath, criteria, page);
	        PaginableDocuments documents = (PaginableDocuments) nuxeoController.executeNuxeoCommand(nuxeoCommand);
	        return documents;
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document, boolean alreadyPinned) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
    	// Portlet request
    	PortletRequest request = portalControllerContext.getRequest();
    	// Internationalization bundle
    	Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
        // Document DTO
        DocumentDTO dto = this.documentDAO.toDTO(document);

        // Vignette property map
        PropertyMap vignettePropertyMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
        // Vignette URL
        String vignetteUrl;
        if ((vignettePropertyMap == null) || vignettePropertyMap.isEmpty()) {
            vignetteUrl = null;
        } else {
            vignetteUrl = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
        }

        // Icon
        String icon = dto.getIcon();

        // Document properties
        Map<String, String> properties = new HashMap<>();
        properties.put("id", document.getString(WEB_ID_PROPERTY));
        String title = document.getTitle();
        if (alreadyPinned) {
        	title += " " + bundle.getString("DOCUMENT_ALREADY_PINNED");
        	properties.put("disabled", "true");
        }
        properties.put("title", title);
        properties.put("vignette", vignetteUrl);
        properties.put("icon", icon);
        properties.put("description", document.getString(DESCRIPTION_PROPERTY));

        return properties;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PinnedDocument getDocument(PortalControllerContext portalControllerContext, String workspacePath, String webid)
    {
    	NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        // Nuxeo command
        INuxeoCommand nuxeoCommand = new GetDocumentCommand(NuxeoQueryFilterContext.CONTEXT_LIVE, workspacePath, webid);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand); 
        
        PinnedDocument pinnedDocument = null;
        
        if (documents != null)
        {
        	Document document = documents.get(0);
        	DocumentDTO dto = documentDAO.toDTO(document);
        	pinnedDocument = new PinnedDocument(dto, document.getString(WEB_ID_PROPERTY), false, 0);
        }
        return pinnedDocument;
    }

}
