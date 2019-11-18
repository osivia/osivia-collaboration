package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface ItemRepository {

	/** RSS document type name. */
	String DOCUMENT_TYPE_EVENEMENT = "RssItem";
	/** Id Conteneur RSS */
	String CONTENEUR_PROPERTY = "rssi:syncId";
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
    * Create Item RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model
    * @throws PortletException
    */
   void creatItem(PortalControllerContext portalControllerContext, ItemRssModel model) throws PortletException;

   /**
    * Create Item RSS.
    *
    * @param portalControllerContext portal controller context
    * @param items
    * @throws PortletException
    */
   void creatItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items) throws PortletException;   
   
   /**
    * remove Item.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   void removeItem(PortalControllerContext portalControllerContext) throws PortletException;
   
   /**
    * remove Items.
    *
    * @param portalControllerContext portal controller context
    * @param items 
    * @throws PortletException
    */
   void removeItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items) throws PortletException;
   
   /**
    * get feed list RSS.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   List<ItemRssModel> getListItemRss(PortalControllerContext portalControllerContext) throws PortletException;   
   
}