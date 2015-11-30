package org.osivia.services.statistics.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.model.StatisticsConfiguration;

/**
 * Statistics service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface IStatisticsService {

    /**
     * Load statistics data.
     *
     * @param portalControllerContext portal controller context
     * @param configuration statistics configuration
     * @return data
     * @throws PortletException
     */
    String loadData(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration) throws PortletException;

}
