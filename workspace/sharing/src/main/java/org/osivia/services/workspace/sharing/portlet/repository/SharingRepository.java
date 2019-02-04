package org.osivia.services.workspace.sharing.portlet.repository;

import java.util.SortedMap;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.common.repository.SharingCommonRepository;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
import org.osivia.services.workspace.sharing.portlet.model.SharingPermission;

/**
 * Sharing portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 * @see SharingCommonRepository
 */
public interface SharingRepository extends SharingCommonRepository {

    /** Sharing link identifier Nuxeo document property. */
    String SHARING_LINK_ID_PROPERTY = "sharing:linkId";
    /** Sharing link permission Nuxeo document property. */
    String SHARING_LINK_PERMISSION_PROPERTY = "sharing:linkPermission";
    /** Sharing banned users Nuxeo document property. */
    String SHARING_BANNED_USERS_PROPERTY = "sharing:bannedUsers";

    /** Elasticsearch synchronized operation flag. */
    String ES_SYNC_FLAG = "nx_es_sync";


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
     * Get users.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return users
     * @throws PortletException
     */
    SortedMap<String, Boolean> getUsers(PortalControllerContext portalControllerContext, String path) throws PortletException;


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


    /**
     * Update permissions.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param permission sharing permission
     * @param user user
     * @param add add or remove permissions indicator
     * @throws PortletException
     */
    void updatePermissions(PortalControllerContext portalControllerContext, String path, SharingPermission permission, String user, Boolean add)
            throws PortletException;

}
