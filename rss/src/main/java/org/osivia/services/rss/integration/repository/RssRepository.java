package org.osivia.services.rss.integration.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.Model.ConteneurRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface RssRepository {

   /** RSS event document type name. */
   String DOCUMENT_TYPE_EVENEMENT = "rss";
	/** Id Conteneur RSS */
	String CONTENEUR_PROPERTY = "rss:idConteneur";
   /** title Item Nuxeo property. */
   String TITLE_PROPERTY = "rss:title";
   /** link Nuxeo property. */
   String LINK_PROPERTY = "rss:link";
   /** Description Nuxeo property. */
   String DESCRIPTION_PROPERTY = "rss:description";
   /** title Item Nuxeo property. */
   String AUTHOR_PROPERTY = "rss:author";   
   /** Category Nuxeo property. */
   String CATEGORY_PROPERTY = "rss:category";
   /** enclosure Nuxeo property. */
   String ENCLOSURE_PROPERTY = "rss:enclosure";
   /** GUID Nuxeo property. */
   String GUID_PROPERTY = "rss:guid";   
   /** pubDate Nuxeo property. */
   String PUBDATE_PROPERTY = "rss:pubDate";
   /** sourceRss Nuxeo property. */
   String SOURCES_PROPERTY = "rss:sourceRss"; 

   /** RSS event document type name. */
   String DOCUMENT_TYPE_CONTENEUR = "conteneur";
   /** Id flux RSS */
   String ID_GUID_PROPERTY = "conrss:idGuid";
   /** Id Conteneur */
   String ID_CONTENEUR_PROPERTY = "conrss:idConteneur";
   /** Title globale du flux RSS */
   String TITLE_CONTENEUR_PROPERTY = "conrss:title";   
   /** Description globale du flux RSS */
   String DESCRIPTION_CONTENEUR_PROPERTY = "conrss:description";
   /** Date globale du flux RSS */
   String PUBDATE_CONTENEUR_PROPERTY = "conrss:pubDate";   
   
   /** List of synchronization sources */
   String LIST_SOURCE_RSS = "rss:sources";
   
   /**
    * Save.
    *
    * @param portalControllerContext portal controller context
    * @param form form
    * @throws PortletException
    */
   void save(PortalControllerContext portalControllerContext, RssForm form) throws PortletException;
   
   /**
    * Remove document using document_id
    *
    * @param portalControllerContext portal controller context
    * @param model 
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext, ConteneurRssModel model) throws PortletException;   
   
}