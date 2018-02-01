package org.osivia.services.pins.plugin.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Pins plugin service
 * @author jbarberet
 *
 */
public interface PinsPluginService {

	/**
	 * Return a list of documentDTO
	 * @param portalControllerContext
	 * @return a list of documentDTO
	 * @throws PortletException
	 */
	List<DocumentDTO> getDocumentsList(PortalControllerContext portalControllerContext) throws PortletException;
	
}
