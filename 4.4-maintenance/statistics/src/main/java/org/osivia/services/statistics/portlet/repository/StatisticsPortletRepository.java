package org.osivia.services.statistics.portlet.repository;

import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.portlet.model.StatisticsWindowSettings;

/**
 * Statistics portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface StatisticsPortletRepository {

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
     * Get documents counts by periods.
     *
     * @param portalControllerContext portal controller context
     * @param configuration statistics configuration
     * @return documents counts
     * @throws PortletException
     */
    Map<String, Integer[]> getDocumentsCountsByPeriods(PortalControllerContext portalControllerContext, StatisticsWindowSettings configuration)
            throws PortletException;


    /**
     * Get space path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getSpacePath(PortalControllerContext portalControllerContext) throws PortletException;

}
