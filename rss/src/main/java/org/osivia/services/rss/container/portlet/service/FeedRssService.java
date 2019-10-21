package org.osivia.services.rss.container.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.container.portlet.model.ContainerRssModel;

/**
 * Container RSS service interface
 * Lecture des conteneur de flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public interface FeedRssService {

    /**
     * get List Container.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return List<ContainerRssModel>
     * @throws PortletException
     */
	List<ContainerRssModel> getListContainer(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * create Container.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return List<ContainerRssModel>
     * @throws PortletException
     */
	void creatContainer(PortalControllerContext portalControllerContext) throws PortletException;	
}
