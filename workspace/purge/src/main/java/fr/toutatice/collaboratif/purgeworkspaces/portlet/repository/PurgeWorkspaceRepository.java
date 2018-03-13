package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceOptions;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.WorkspaceLine;

/** 
 * Purge workspace repository
 * @author Julien Barberet
 *
 */
public interface PurgeWorkspaceRepository {

	/** Get list of workspace with order criteria */
	List<WorkspaceLine> getListWorkspace(PortalControllerContext portalControllerContext, PurgeWorkspaceOptions options,
			String sortColumn, String sortOrder, int pageNumber, int pageSize);
	
	/** Put a list of workspace in trash */
	void putInTrash(PortalControllerContext portalControllerContext, List<String> listId);
	
	/** Purge the workspaces that are in bin */
	void purge(PortalControllerContext portalControllerContext, Documents documents);
	
	/** Restore a workspace */
	void restore(PortalControllerContext portalControllerContext, String uid);
	
	/** Get workspaces in bin */
	Documents getDeletedWorkspaces(PortalControllerContext portalControllerContext);
}
