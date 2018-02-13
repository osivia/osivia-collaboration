package fr.toutatice.collaboratif.purgeworkspaces.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceForm;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceOptions;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.WorkspaceLine;

/**
 * Purge workspace service
 * @author Julien Barberet
 *
 */
public interface PurgeWorkspacesService {
	
	/**
	 * Get form
	 * @param portalControllerContext
	 * @return
	 * @throws PortletException
	 */
	PurgeWorkspaceForm getForm(PortalControllerContext portalControllerContext) throws PortletException;
	
	/**
	 * Get Options
	 * @param portalControllerContext
	 * @param sort
	 * @param alt
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws PortletException
	 */
	PurgeWorkspaceOptions getOptions(PortalControllerContext portalControllerContext, 
			String sort, String alt, String pageNumber, String pageSize) throws PortletException;
	
	/**
	 * Put in bin a list of workspaces
	 * @param portalControllerContext
	 * @param form
	 */
	void putInBin(PortalControllerContext portalControllerContext, PurgeWorkspaceForm form);
	
	/**
	 * Restore a workspace
	 * @param portalControllerContext
	 * @param uid
	 */
	void restore(PortalControllerContext portalControllerContext, String uid);
	
	/**
	 * Purge a list of workspaces
	 * @param portalControllerContext
	 * @param listIdToPurge
	 */
	int purge(PortalControllerContext portalControllerContext);
	
	/**
	 * Load the workspaces with order criteria
	 * @param portalControllerContext
	 * @param options
	 * @return
	 */
	List<WorkspaceLine> loadList(PortalControllerContext portalControllerContext, PurgeWorkspaceOptions options);
}
