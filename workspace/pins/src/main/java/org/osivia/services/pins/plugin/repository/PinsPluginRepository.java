package org.osivia.services.pins.plugin.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Pins plugin repository
 * @author jbarberet
 *
 */
public interface PinsPluginRepository {

	/**
	 * Get documents list from list of webId
	 * @param portalControllerContext
	 * @param workspacePath
	 * @param listwebid
	 * @return
	 */
	List<DocumentDTO> getDocumentsList(PortalControllerContext portalControllerContext, String workspacePath, List<Object> listwebid);
	
	/**
	 * Get workspace
	 * @param portalControllerContext
	 * @return
	 * @throws PortletException
	 */
    Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException;
}
