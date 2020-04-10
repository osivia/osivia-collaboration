package org.osivia.services.rss.common.repository;

import java.util.List;
import java.util.Set;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface ContainerRepository {

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
	/** Logo flux RSS */
	String LOGO_PROPERTY = "logos";	
    /** CMS path window property name. */
    String CMS_PATH_WINDOW_PROPERTY = "osivia.feed.cmsPath";	
	   
   /**
    * Create container RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model
    * @throws PortletException
    */
	void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
	

   /**
    * remove container flux RSS.
    *
    * @param portalControllerContext portal controller context
    * @param docid String
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext, String docid) throws PortletException;   

   /**
    * get containers list RSS.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   List<ContainerRssModel> getListContainerRss(PortalControllerContext portalControllerContext) throws PortletException;
   
   /**
    * get Map Container.
    *
    * @param portalControllerContext portal controller context
    * @return Map<Integer, String> 
    * @throws PortletException
    */
	Set<String> getMapContainer(PortalControllerContext portalControllerContext) throws PortletException;

	/**
	 * Modification container RSS.
	 *
	 * @param portalControllerContext portal controller context
	 * @param model
	 * @throws PortletException
	 */
	void modifContainer(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException;
}