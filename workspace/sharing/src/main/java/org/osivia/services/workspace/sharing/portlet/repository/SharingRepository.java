package org.osivia.services.workspace.sharing.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.common.repository.SharingCommonRepository;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
import org.osivia.services.workspace.sharing.portlet.model.SharingPermission;

import javax.portlet.PortletException;
import java.util.SortedMap;

/**
 * Sharing portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see SharingCommonRepository
 */
public interface SharingRepository extends SharingCommonRepository {

    /**
     * Elasticsearch synchronized operation flag.
     */
    String ES_SYNC_FLAG = "nx_es_sync";


    /**
     * Check if sharing is enabled.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return true if sharing is enabled
     * @throws PortletException
     */
    boolean isSharingEnabled(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get sharing link.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return sharing link
     * @throws PortletException
     */
    SharingLink getLink(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get users.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return users
     * @throws PortletException
     */
    SortedMap<String, Boolean> getUsers(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Enable sharing.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param link                    sharing link
     * @throws PortletException
     */
    void enableSharing(PortalControllerContext portalControllerContext, String path, SharingLink link) throws PortletException;


    /**
     * Disable sharing.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @throws PortletException
     */
    void disableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Update permissions.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param permission              sharing permission
     * @param user                    user
     * @param add                     add or remove permissions indicator
     * @param ban                     ban or unban user indicator
     * @throws PortletException
     */
    void updatePermissions(PortalControllerContext portalControllerContext, String path, SharingPermission permission, String user, Boolean add, Boolean ban) throws PortletException;

}
