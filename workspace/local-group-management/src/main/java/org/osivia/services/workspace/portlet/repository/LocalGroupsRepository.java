package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummaryItem;

/**
 * Local groups portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface LocalGroupsRepository {

    /**
     * Get workspace identifier.
     * 
     * @param portalControllerContext portal controller context
     * @return workspace identifier
     * @throws PortletException
     */
    String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get local groups summary items.
     *
     * @param portalControllerContext portal controller context
     * @return local groups
     * @throws PortletException
     */
    List<LocalGroupsSummaryItem> getLocalGroupsSummaryItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get workspace members.
     * 
     * @param portalControllerContext portal controller context
     * @return members
     * @throws PortletException
     */
    List<WorkspaceMember> getWorkspaceMembers(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get local group.
     * 
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return local group
     * @throws PortletException
     */
    CollabProfile getLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Get local group members.
     * 
     * @param portalControllerContext portal controller context
     * @param id local group identifier
     * @return members
     * @throws PortletException
     */
    List<Person> getLocalGroupMembers(PortalControllerContext portalControllerContext, String id) throws PortletException;


    /**
     * Edit local group.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void editLocalGroup(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException;


    /**
     * Create local group.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException;


    /**
     * Delete local groups.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selection identifiers
     * @throws PortletException
     */
    void deleteLocalGroups(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException;


    /**
     * Add members to local groups
     * 
     * @param portalControllerContext portal controller context
     * @param members member identifiers
     * @param groups group identifiers
     * @throws PortletException
     */
    void addMembersToLocalGroups(PortalControllerContext portalControllerContext, List<String> members, List<String> groups) throws PortletException;

}
