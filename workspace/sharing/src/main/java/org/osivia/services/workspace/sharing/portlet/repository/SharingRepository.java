package org.osivia.services.workspace.sharing.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Sharing portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingRepository {

    /** Sharing facet. */
    String SHARING_FACET = "Sharing";


    /**
     * Check if sharing is enabled.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return true if sharing is enabled
     * @throws PortletException
     */
    boolean isSharingEnabled(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Enable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @throws PortletException
     */
    void enableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Disable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @throws PortletException
     */
    void disableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
