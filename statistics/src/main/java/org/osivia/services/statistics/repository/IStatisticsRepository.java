package org.osivia.services.statistics.repository;

import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.model.StatisticsConfiguration;

/**
 * Statistics repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface IStatisticsRepository {

    /**
     * Get statistics configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    StatisticsConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save statistics configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration statistics configuration
     * @throws PortletException
     */
    void saveConfiguration(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration) throws PortletException;


    /**
     * Get documents count by periods.
     *
     * @param portalControllerContext portal controller context
     * @param configuration statistics configuration
     * @return documents count
     * @throws PortletException
     */
    Map<String, Integer> getDocumentsCountByPeriods(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration)
            throws PortletException;

}
