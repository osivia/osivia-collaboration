package org.osivia.services.workspace.portlet.repository;

import java.util.List;
import java.util.SortedSet;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.Record;
import org.osivia.services.workspace.portlet.model.Role;
import org.osivia.services.workspace.portlet.model.SynthesisNode;

/**
 * Workspace ACL management repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface AclManagementRepository {

    /**
     * Get workspace identifier.
     *
     * @param portalControllerContext portal controller context
     * @return identifier
     * @throws PortletException
     */
    String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return ACL entries
     * @throws PortletException
     */
    AclEntries getAclEntries(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Get roles.
     *
     * @param portalControllerContext portal controller context
     * @return roles
     * @throws PortletException
     */
    List<Role> getRoles(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get default role.
     *
     * @param portalControllerContext portal controller context
     * @return role
     * @throws PortletException
     */
    Role getDefaultRole(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get group records.
     *
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return records
     * @throws PortletException
     */
    List<Record> getGroupRecords(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Get user records.
     *
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return records
     * @throws PortletException
     */
    List<Record> getUserRecords(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;


    /**
     * Update ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @param roles roles
     * @throws PortletException
     */
    void update(PortalControllerContext portalControllerContext, AclEntries entries, List<Role> roles) throws PortletException;


    /**
     * Cancel update ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @throws PortletException
     */
    void cancelUpdate(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException;


    /**
     * Add ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @param records added records
     * @param role added role
     * @throws PortletException
     */
    void add(PortalControllerContext portalControllerContext, AclEntries entries, List<Record> records, Role role) throws PortletException;


    /**
     * Reset ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @throws PortletException
     */
    void reset(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException;


    /**
     * Get synthesis nodes.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return synthesis nodes
     * @throws PortletException
     */
    SortedSet<SynthesisNode> getSynthesisNodes(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException;

}
