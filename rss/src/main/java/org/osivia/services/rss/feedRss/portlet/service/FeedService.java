package org.osivia.services.rss.feedRss.portlet.service;

import java.io.IOException;
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
     * get List Feed.
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
     * @param model FeedRssModel
     * @throws PortletException
     */
	void creatFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException;	

    /**
     * synchronization Feed with Items.
     *
     * @param portalControllerContext portal controller context
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
    Map<String, String> getMapFeed(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * Modification Feed.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void modFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException;	

    /**
     * Delete Feed.
     *
     * @param portalControllerContext portal controller context
     * @param model FeedRssModel
     * @throws PortletException
     */
	void delFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException;

    /**
     * get Map Feed.
     *
     * @param portalControllerContext portal controller context
     * @param id syncId
     * @param name 
     * @param url
     * @return FeedRssModel
     * @throws PortletException
     */
	FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url) throws PortletException;

    /**
     * get Map Container.
     *
     * @param portalControllerContext portal controller context
     * @return FeedRssModel
     * @throws PortletException
     */
	ContainerRssModel getMapContainer(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * Modif Container.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void modifContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException, IOException;
	
    /**
     * remove Container.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void removeContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException, IOException;	
	
    /**
     * Upload visual.
     * 
     * @param portalControllerContext portal controller context
     * @param form FeedRssModel 
     * @throws PortletException
     * @throws IOException
     */
    void uploadVisual(PortalControllerContext portalControllerContext, FeedRssModel form) throws PortletException, IOException;


    /**
     * Delete visual.
     * 
     * @param portalControllerContext portal controller context
     * @param form FeedRssModel 
     * @throws PortletException
     */
    void deleteVisual(PortalControllerContext portalControllerContext, FeedRssModel form) throws PortletException;    
    
    /**
     * Set Visual 
     * 
     * @param portalControllerContext portal controller context
     * @param form FeedRssModel 
     * @throws PortletException
     */
    void setVisual(PortalControllerContext portalControllerContext, FeedRssModel form) throws PortletException;
}
