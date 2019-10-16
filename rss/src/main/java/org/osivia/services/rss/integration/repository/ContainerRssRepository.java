package org.osivia.services.rss.integration.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.Model.ContainerRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface ContainerRssRepository {

	/** RSS event document type name. */
	String DOCUMENT_TYPE_CONTENEUR = "ottc-rss-container";
	/** Id flux RSS */
	String ID_GUID_PROPERTY = "rssc:syncId";
	/** Id Conteneur */
	String ID_CONTENEUR_PROPERTY = "rssc:idConteneur";
	/** Title globale du flux RSS */
	String TITLE_CONTENEUR_PROPERTY = "rssc:title";
	/** Description globale du flux RSS */
	String DESCRIPTION_CONTENEUR_PROPERTY = "rssc:description";
	/** Date globale du flux RSS */
	String PUBDATE_CONTENEUR_PROPERTY = "rssc:pubDate";
	   
   /**
    * Creation du container RSS.
    *
    * @param portalControllerContext portal controller context
    * @param Model
    * @throws PortletException
    */
   void creation(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
   
   /**
    * Suppresion du container de flux RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model 
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;   

   /**
    * liste des containers de flux RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model 
    * @throws PortletException
    */
   void getListContainerRss(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
}