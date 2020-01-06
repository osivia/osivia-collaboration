package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface FeedRepository {

	/** RSS document type name. */
	String DOCUMENT_TYPE_CONTENEUR = "RssContainer";
	/** DC title RSS */
	String NAME_PROPERTY = "dc:title";
	/** FEEDS RSS */
	String FEEDS_PROPERTY = "rssc:feeds";
	/** Display Name RSS */
	String DISPLAY_NAME_PROPERTY = "displayName";	
	/** url du flux RSS */
	String URL_PROPERTY = "url";
	/** Id sync flux RSS */
	String ID_PROPERTY = "syncId";
   
   /**
    * remove Feed.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext) throws PortletException;
   
   /**
    * Create feed RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model
    * @throws PortletException
    */
	void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;	
      
	/**
	 * get feeds list RSS.
	 *
	 * @param portalControllerContext portal controller context
	 * @throws PortletException
	 */
	ContainerRssModel getListFeedRss(PortalControllerContext portalControllerContext) throws PortletException;
   
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
     * @return Set<String> 
     * @throws PortletException
     */
    Map<String, String> getMapFeed(PortalControllerContext portalControllerContext) throws PortletException;    

    /**
     * get Map Feed.
     *
     * @param portalControllerContext portal controller context
     * @param id syncId
     * @return FeedRssModel
     * @throws PortletException
     */
 	FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url) throws PortletException;
 	
 	/**
     * Modification feed RSS.
     *
     * @param portalControllerContext portal controller context
     * @param model
     * @throws PortletException
     */
 	void modFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;	
 	
 	
    /**
     * Delete feed RSS.
     *
     * @param portalControllerContext portal controller context
     * @param model
     * @throws PortletException
     */
 	void delFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;

}