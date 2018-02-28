package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.Participants;

/**
 * Participants portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface ParticipantsService {

    /** Task identifier. */
    String TASK_ID = "WORKSPACE_PARTICIPANTS";


    /**
     * Get configuration.
     * 
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save configuration.
     * 
     * @param portalControllerContext portal controller context
     * @param configuration configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;


    /**
     * Get workspace participants.
     * 
     * @param portalControllerContext portal controller context
     * @return participants
     * @throws PortletException
     */
    Participants getParticipants(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get panel player.
     * 
     * @param portalControllerContext portal controller context
     * @return player
     * @throws PortletException
     */
    PanelPlayer getPlayer(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get more URL.
     * 
     * @param portalControllerContext portal controller context
     * @return URL
     * @throws PortletException
     */
    String getMoreUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
