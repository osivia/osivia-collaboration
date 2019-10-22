package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface FeedRepository {

	/** RSS document type name. */
	String DOCUMENT_TYPE_EVENEMENT = "ottc-rss-item";
	/** Id Conteneur RSS */
	String CONTENEUR_PROPERTY = "rssi:idConteneur";
	/** title Item Nuxeo property. */
	String TITLE_PROPERTY = "rssi:title";
	/** link Nuxeo property. */
	String LINK_PROPERTY = "rssi:link";
	/** Description Nuxeo property. */
	String DESCRIPTION_PROPERTY = "rssi:description";
	/** title Item Nuxeo property. */
	String AUTHOR_PROPERTY = "rssi:author";
	/** Category Nuxeo property. */
	String CATEGORY_PROPERTY = "rssi:category";
	/** enclosure Nuxeo property. */
	String ENCLOSURE_PROPERTY = "rssi:enclosure";
	/** GUID Nuxeo property. */
	String GUID_PROPERTY = "rssi:guid";
	/** pubDate Nuxeo property. */
	String PUBDATE_PROPERTY = "rssi:pubDate";
	/** source Nuxeo property. */
	String SOURCES_PROPERTY = "rssi:source";	   
   
   /**
    * Create feed RSS.
    *
    * @param portalControllerContext portal controller context
    * @param Model
    * @throws PortletException
    */
   void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
   
   /**
    * remove Feed.
    *
    * @param portalControllerContext portal controller context
    * @param model 
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext) throws PortletException;
   
   /**
    * get feed list RSS.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   List<ContainerRssModel> getListFeedRss(PortalControllerContext portalControllerContext) throws PortletException;   
   
}