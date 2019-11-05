package org.osivia.services.rss.feedRss.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;

/**
 * Container RSS service interface
 * Lecture des conteneur de flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public interface FeedService {

    /**
     * get List Container.
     *
     * @param portalControllerContext portal controller context
     * @return List<ContainerRssModel>
     * @throws PortletException
     */
	List<ContainerRssModel> getListFeed(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * create Feed.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;	

    /**
     * synchronization Feed with Items.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void synchro(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;

}
