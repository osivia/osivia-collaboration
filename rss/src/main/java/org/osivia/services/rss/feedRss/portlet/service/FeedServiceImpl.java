package org.osivia.services.rss.feedRss.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.osivia.services.rss.common.utility.RssUtility;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.osivia.services.rss.feedRss.portlet.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RSS service interface
 * Lecture des flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
@Service
public class FeedServiceImpl implements FeedService {

    /** Repository. */
    @Autowired
    public ContainerRepository repository;
	
    /** Repository. */
    @Autowired
    public ItemRepository repositoryItem;
    
    /** logger */
	protected static final Log logger = LogFactory.getLog(FeedServiceImpl.class);

	private static final ContainerRssModel feed = null;

    /**
     * {@inheritDoc}
     */
    public List<ContainerRssModel> getListFeed(PortalControllerContext portalControllerContext) throws PortletException {

    	List<ContainerRssModel> containers = this.repository.getListContainerRss(portalControllerContext);    	
        return containers;
    }

    public void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {

    	this.repository.creatFeed(portalControllerContext, model);    	
    }
    
    public void synchro(PortalControllerContext portalControllerContext) throws PortletException {

    	// Recherche la liste des feeds
    	List<ContainerRssModel> feeds = this.repository.getListContainerRss(portalControllerContext);
    	
    	for(ContainerRssModel feed : feeds) {
        	// retourne une map d'item à faire correspondre avec les items déjà enregistré
			List<ItemRssModel> items = RssUtility.readRss(feed);
    		
    		// if la liste est vide on ne supprime pas d'Items dans Nuxeo
    			// Doit-on le faire si <5 ?
    		if(items.size() >= 1) {
            	// Recherche la liste des Items correspondant au flux
            	List<ItemRssModel> itemsNuxeo = this.repositoryItem.getListItemRss(portalControllerContext);    			

            	// Comparaison de la liste restituée par la lecture du flux et les Items présents dans Nuxeo
            	// Si Item Nuxeo présent dans le flux lut alors on le supprime de la liste (afin de ne pas le recréer)
            	items.removeAll(itemsNuxeo);
            	this.repositoryItem.creatItems(portalControllerContext, items); 

            	itemsNuxeo.removeAll(items);
            	
            	// Tous les Item Nuxeo pas trouver dans le flux seront supprimé
            	this.repositoryItem.removeItems(portalControllerContext, itemsNuxeo);
    		}
    	}
    }

	@Override
	public void synchro(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException {
		// TODO Auto-generated method stub
		
	}    
}
