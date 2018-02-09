package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;

/** 
 * Purge workspace repository
 * @author Julien Barberet
 *
 */
public interface PurgeWorkspaceRepository {

	/** Get list of workspace with order criteria */
	PaginableDocuments getListWorkspace(PortalControllerContext portalControllerContext, 
			String sortColumn, String sortOrder, int pageNumber, int pageSize);
	
	/** Put a list of workspace in bin */
	void putInBin(PortalControllerContext portalControllerContext, List<String> listId);
	
	/** Purge the workspaces that are in bin */
	void purge(PortalControllerContext portalControllerContext, List<String> listIdToPurge);
	
	/** Restore a workspace */
	void restore(PortalControllerContext portalControllerContext, String uid);
}
