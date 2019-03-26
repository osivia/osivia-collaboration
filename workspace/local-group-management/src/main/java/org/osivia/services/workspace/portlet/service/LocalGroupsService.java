package org.osivia.services.workspace.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.AbstractLocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;

/**
 * Local groups portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface LocalGroupsService {

    /**
     * Get all members.
     * 
     * @param portalControllerContext portal controller
     * @return members
     * @throws PortletException
     */
    List<LocalGroupMember> getAllMembers(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Convert person to member.
     * 
     * @param person person
     * @return member
     */
    LocalGroupMember convertPersonToMember(Person person);


    /**
     * Get local groups summary.
     * 
     * @param portalControllerContext portal controller context
     * @return summary
     * @throws PortletException
     */
    LocalGroupsSummary getSummary(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get selection.
     * 
     * @param portalControllerContext portal controller context
     * @param summary local groups summary
     * @param identifiers selection identifiers
     * @return selection
     * @throws PortletException
     */
    List<AbstractLocalGroup> getSelection(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, String[] identifiers)
            throws PortletException;

}
