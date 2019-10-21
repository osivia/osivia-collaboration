package org.osivia.services.rss.container.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.container.portlet.model.ItemRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface RssRepository {

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
    * Creation du flux RSS.
    *
    * @param portalControllerContext portal controller context
    * @param Model
    * @throws PortletException
    */
   void creation(PortalControllerContext portalControllerContext, ItemRssModel model) throws PortletException;
   
   /**
    * Suppresion du  flux.
    *
    * @param portalControllerContext portal controller context
    * @param model 
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext, ItemRssModel model) throws PortletException;   
   
}