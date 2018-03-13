package org.osivia.services.statistics.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.portlet.model.CreationsView;
import org.osivia.services.statistics.portlet.model.StatisticsForm;
import org.osivia.services.statistics.portlet.model.StatisticsWindowSettings;
import org.osivia.services.statistics.portlet.model.VisitsView;

import net.sf.json.JSONObject;

/**
 * Statistics portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface StatisticsPortletService {

    /**
     * Get window settings.
     * 
     * @param portalControllerContext portal controller context
     * @return window settings
     * @throws PortletException
     */
    StatisticsWindowSettings getWindowSettings(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save window settings.
     * 
     * @param portalControllerContext portal controller context
     * @param windowSettings window settings
     * @throws PortletException
     */
    void saveWindowSettings(PortalControllerContext portalControllerContext, StatisticsWindowSettings windowSettings) throws PortletException;


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    StatisticsForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get creations.
     * 
     * @param portalControllerContext portal controller context
     * @param view creations view
     * @return JSON
     * @throws PortletException
     */
    JSONObject getCreations(PortalControllerContext portalControllerContext, CreationsView view) throws PortletException;


    /**
     * Get visits.
     * 
     * @param portalControllerContext portal controller context
     * @param view visits view
     * @return JSON
     * @throws PortletException
     */
    JSONObject getVisits(PortalControllerContext portalControllerContext, VisitsView view) throws PortletException;

}
