package org.osivia.services.rss.common.repository;

import java.util.List;

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
	/** Id sync flux RSS */
	String ID_PROPERTY = "rssc:syncId";
	/** Id partener */
	String ID_PART_PROPERTY = "rssc:partId";
	/** url du flux RSS */
	String URL_PROPERTY = "rssc:url";
	/** Display Name RSS */
	String DISPLAY_NAME_PROPERTY = "rssc:displayName";
	/** dc title RSS */
	String NAME_PROPERTY = "dc:title";
    /** CMS path window property name. */
    String CMS_PATH_WINDOW_PROPERTY = "osivia.container.cmsPath";	
	   
   /**
    * Create container RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model
    * @throws PortletException
    */
	void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
	
   /**
    * Create feed RSS.
    *
    * @param portalControllerContext portal controller context
    * @param model
    * @throws PortletException
    */
	void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;	
   
   /**
    * remove container flux RSS.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   void remove(PortalControllerContext portalControllerContext) throws PortletException;   

   /**
    * get containers list RSS.
    *
    * @param portalControllerContext portal controller context
    * @throws PortletException
    */
   List<ContainerRssModel> getListContainerRss(PortalControllerContext portalControllerContext) throws PortletException;
}