package org.osivia.services.workspace.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AddForm;
import org.osivia.services.workspace.portlet.model.Role;

/**
 * Workspace ACL management service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface AclManagementService {

    /**
     * Get ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @return ACL entries
     * @throws PortletException
     */
    AclEntries getAclEntries(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get add form.
     *
     * @param portalControllerContext portal controller context
     * @return add form
     * @throws PortletException
     */
    AddForm getAddForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get roles.
     *
     * @param portalControllerContext portal controller context
     * @return roles
     * @throws PortletException
     */
    List<Role> getRoles(PortalControllerContext portalControllerContext) throws PortletException;


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
     * Add ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @param roles roles
     * @param form add form
     * @throws PortletException
     */
    void add(PortalControllerContext portalControllerContext, AclEntries entries, List<Role> roles, AddForm form) throws PortletException;


    /**
     * Reset ACL entries.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @throws PortletException
     */
    void reset(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException;


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param entries ACL entries
     * @return URL
     * @throws PortletException
     */
    String getRedirectionUrl(PortalControllerContext portalControllerContext, AclEntries entries) throws PortletException;

}
