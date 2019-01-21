package org.osivia.services.workspace.sharing.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;

/**
 * Sharing portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingRepository {

    /** Sharing facet. */
    String SHARING_FACET = "Sharing";

    /** Sharing link identifier Nuxeo document property. */
    String SHARING_LINK_ID_PROPERTY = "sharing:linkId";
    /** Sharing link permission Nuxeo document property. */
    String SHARING_LINK_PERMISSION_PROPERTY = "sharing:linkPermission";


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
     * Get sharing link.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return sharing link
     * @throws PortletException
     */
    SharingLink getLink(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Enable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param link sharing link
     * @throws PortletException
     */
    void enableSharing(PortalControllerContext portalControllerContext, String path, SharingLink link) throws PortletException;


    /**
     * Disable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @throws PortletException
     */
    void disableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
