package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.Group;

/**
 * Participants portlet repository.
 * 
 * @author Cédric Krommenhoek
 */
public interface ParticipantsRepository {

    /** View window property. */
    String VIEW_WINDOW_PROPERTY = "participants.view";
    /** Display window property prefix. */
    String DISPLAY_WINDOW_PROPERTY_PREFIX = "participants.display.";
    /** Max window property. */
    String MAX_WINDOW_PROPERTY = "participants.max";

    /** Max default value. */
    int MAX_DEFAULT_VALUE = 8;


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
     * Get groups.
     * 
     * @param portalControllerContext portal controller context
     * @return groups
     * @throws PortletException
     */
    List<Group> getGroups(PortalControllerContext portalControllerContext) throws PortletException;

}
