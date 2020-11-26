package org.osivia.services.editor.common.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Portlet common repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CommonRepository {

    /**
     * Get document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document
     */
    Document getDocument(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
