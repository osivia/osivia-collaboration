package org.osivia.services.rss.common.repository;

import java.util.List;
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
	/** Logos flux RSS */
	String LOGO_PROPERTY = "logos";
   
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
     * @param name 
     * @param url
     * @param index
     * @return FeedRssModel
     * @throws PortletException
     */
 	FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url, int index) throws PortletException;
 	
 	/**
     * Modification feed RSS.
     *
     * @param portalControllerContext portal controller context
     * @param model
     * @throws PortletException
     */
 	void modFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException;	

 	
    /**
     * Delete feed RSS.
     *
     * @param portalControllerContext portal controller context
     * @param model
     * @throws PortletException
     */
 	void delFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException;

 	/**
 	 * Fill Feeds
     * 
     * @param Document document
     * @throws PortletException
 	 * */
 	List<FeedRssModel> fillFeed(Document document) throws PortletException;;
}