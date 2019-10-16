package org.osivia.services.rss.integration.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.Model.ContainerRssModel;

/**
 * Container RSS service interface
 * Lecture des conteneur de flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public interface ContainerRssService {

    /**
     * get List Container.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return calendar events
     * @throws PortletException
     */
    void getListContainer(PortalControllerContext portalControllerContext, ContainerRssModel containerRss) throws PortletException;

}
