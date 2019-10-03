package org.osivia.services.search.common.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Common repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CommonRepository {

    /**
     * Get root.
     * 
     * @param portalControllerContext portal controller context
     * @return root document
     * @throws PortletException
     */
	Document getRoot(PortalControllerContext portalControllerContext) throws PortletException;

}
