package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.ListWorkspaceCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.PurgeCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.PutInTrashWorkspaceCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.RestoreWorkspaceCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Purge workspace repository implementation
 * @author Julien Barberet
 *
 */
@Repository
public class PurgeWorkspaceRepositoryImpl implements PurgeWorkspaceRepository {

	/**
     * {@inheritDoc}
     */
    @Override
	public PaginableDocuments getListWorkspace(PortalControllerContext portalControllerContext, 
			String sortColumn, String sortOrder, int pageNumber, int pageSize)
	{
	     // Nuxeo controler
	   	 NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
	
	     // Nuxeo command
	     INuxeoCommand nuxeoCommand = new ListWorkspaceCommand(NuxeoQueryFilterContext.CONTEXT_LIVE, sortColumn, sortOrder, pageNumber, pageSize);
	     PaginableDocuments documents = (PaginableDocuments) nuxeoController.executeNuxeoCommand(nuxeoCommand);    
	     return documents;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void putInBin(PortalControllerContext portalControllerContext, List<String> listId) {
		
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		for(String uid:listId)
		{
			// Nuxeo command
			INuxeoCommand nuxeoCommand = new PutInTrashWorkspaceCommand(uid);
		    nuxeoController.executeNuxeoCommand(nuxeoCommand);
		}
		
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void purge(PortalControllerContext portalControllerContext, List<String> listIdToPurge) {
		
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		// Nuxeo command
		INuxeoCommand nuxeoCommand = new PurgeCommand(listIdToPurge);
	    nuxeoController.executeNuxeoCommand(nuxeoCommand);
		
		
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void restore(PortalControllerContext portalControllerContext, String uid) {
		
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		
		// Nuxeo command
		INuxeoCommand nuxeoCommand = new RestoreWorkspaceCommand(uid);
		nuxeoController.executeNuxeoCommand(nuxeoCommand);
		
	}
    
    
	
}
