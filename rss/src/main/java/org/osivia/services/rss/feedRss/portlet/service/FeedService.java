package org.osivia.services.rss.feedRss.portlet.service;

import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;

/**
 * Feed RSS service interface
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
	ContainerRssModel getListFeed(PortalControllerContext portalControllerContext) throws PortletException;
	
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
	void synchro(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * Get current Nuxeo document.
     * 
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * get Map Feed.
     *
     * @param portalControllerContext portal controller context
     * @return Map
     * @throws PortletException
     */
	Map<Integer, String> getMapFeed(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * Modification Feed.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void modFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;	

    /**
     * Delete Feed.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void delFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;

    /**
     * get Map Feed.
     *
     * @param portalControllerContext portal controller context
     * @param id syncId
     * @param FeedRssModel model
     * @return FeedRssModel
     * @throws PortletException
     */
	FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, FeedRssModel model) throws PortletException;
}
