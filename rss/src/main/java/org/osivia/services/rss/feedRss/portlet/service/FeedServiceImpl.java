package org.osivia.services.rss.feedRss.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.utility.RssUtility;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.osivia.services.rss.feedRss.portlet.repository.FeedRepository;
import org.osivia.services.rss.feedRss.portlet.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    public FeedRepository repository;
	
    /** Repository. */
    @Autowired
    public ItemRepository repositoryItem;
    
    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;    
    
    /** logger */
	protected static final Log logger = LogFactory.getLog(FeedServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    public ContainerRssModel getListFeed(PortalControllerContext portalControllerContext) throws PortletException {
  	
        return this.repository.getListFeedRss(portalControllerContext);        
    }

    public void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {

    	this.repository.creatFeed(portalControllerContext, model);    	
    }
    
    public void synchro(PortalControllerContext portalControllerContext) throws PortletException {

    	ContainerRssModel model = null;
    	// Recherche la liste des feeds
    	model = this.repository.getListFeedRss(portalControllerContext);
    	
    	for(FeedRssModel feed : model.getFeedSources()) {
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
