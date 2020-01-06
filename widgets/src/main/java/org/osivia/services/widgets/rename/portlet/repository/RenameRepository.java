package org.osivia.services.widgets.rename.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Rename portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface RenameRepository {

    /**
     * Get document context.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return document context
     * @throws PortletException
     */
    NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Rename document.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param title document title
     * @throws PortletException
     */
    void rename(PortalControllerContext portalControllerContext, String path, String title) throws PortletException;

}
