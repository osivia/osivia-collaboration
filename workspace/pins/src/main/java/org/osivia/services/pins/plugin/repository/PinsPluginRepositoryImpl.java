package org.osivia.services.pins.plugin.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.pins.edition.portlet.repository.command.ListDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Pins plugin repository implementation
 * @author jbarberet
 *
 */
@Repository
public class PinsPluginRepositoryImpl implements PinsPluginRepository {
	
    /** Current workspace attribute name. */
    private static final String CURRENT_WORKSPACE_ATTRIBUTE = "osivia.workspace.edition.currentWorkspace";
    
    private static final String WEBID_PROPERTY = "ttc:webid";
	
    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDAO;
	
    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentDTO> getDocumentsList(PortalControllerContext portalControllerContext, String workspacePath, List<Object> listwebid)
    {
    	 NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                 portalControllerContext.getPortletCtx());

         // Nuxeo command
         INuxeoCommand nuxeoCommand = new ListDocumentsCommand(NuxeoQueryFilterContext.CONTEXT_LIVE, listwebid, workspacePath, false);
         Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);    
         
         HashMap<String, DocumentDTO> map = new HashMap<>();
         
         List<DocumentDTO> listDTO = new ArrayList<>();
         
         //The result list is not sorted because of the in clause
         //First put dto in a hashmap
         if (documents != null)
         {
        	 for (int i=0; i< documents.size(); i++)
        	 {
                 map.put(documents.get(i).getString(WEBID_PROPERTY), documentDAO.toDTO(documents.get(i)));
        	 }
         }
         //Then add dto in a list in the order of the listwebid
         for (Object webid: listwebid)
         {
        	 listDTO.add(map.get(webid));
         }
         
         return listDTO;
    }

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
    
}
