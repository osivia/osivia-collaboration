package org.osivia.services.rss.feedRss.portlet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.repository.ContainerRepository;
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

    /** Repository Feed. */
    @Autowired
    public FeedRepository repository;
	
    /** Repository Item. */
    @Autowired
    public ItemRepository repositoryItem;
    
    /** Repository Container. */
    @Autowired
    public ContainerRepository repositoryContainer;
    
    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;    
    
    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;    
    
    /** logger */
	protected static final Log logger = LogFactory.getLog(FeedServiceImpl.class);
	
    /**
     * {@inheritDoc}
     */
    public ContainerRssModel getListFeed(PortalControllerContext portalControllerContext) throws PortletException {
  	
        return this.repository.getListFeedRss(portalControllerContext);        
    }

    public void creatFeed(PortalControllerContext portalControllerContext, FeedRssModel model) throws PortletException {

    	ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
    	List<FeedRssModel> list = new ArrayList<FeedRssModel>();
    	list.add(model);
    	container.setFeedSources(list);
    	this.repository.creatFeed(portalControllerContext, container);
    }
    
	public void synchro(PortalControllerContext portalControllerContext) throws PortletException {

		// Recherche la liste des feeds
		ContainerRssModel model = this.repository.getListFeedRss(portalControllerContext);

		for (FeedRssModel feed : model.getFeedSources()) {
			// retourne une map d'item à faire correspondre avec les items déjà enregistré
			List<ItemRssModel> items = RssUtility.readRss(feed);

			// Si la liste est vide on ne supprime pas d'Items dans Nuxeo
			// Doit-on le faire si <5 ?
			if (items != null) {
				if (items.size() >= 1) {
					// Recherche la liste des Items correspondant au flux
					List<ItemRssModel> itemsNuxeo = this.repositoryItem.getListItemRss(portalControllerContext);

					List<ItemRssModel> itemsSav = items;
					// Comparaison de la liste restituée par la lecture du flux et les Items
					// présents dans Nuxeo
					// Si Item Nuxeo présent dans le flux lut alors on le supprime de la liste (afin
					// de ne pas le recréer)
					if (itemsNuxeo.size() != 0) {
						items.removeAll(itemsNuxeo);
					}

					if (items != null && items.size() != 0) {
						this.repositoryItem.creatItems(portalControllerContext, items);
					}

					if (itemsNuxeo != null) {
						itemsNuxeo.removeAll(itemsSav);
						if (itemsNuxeo != null && itemsNuxeo.size() != 0) {
							// Tous les Item Nuxeo pas trouver dans le flux seront supprimé
							this.repositoryItem.removeItems(portalControllerContext, itemsNuxeo);
						}
					}
				}

			}
		}
	}

	public Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException {
		return this.repository.getCurrentDocument(portalControllerContext);
	}
	
    public Set<String> getMapFeed(PortalControllerContext portalControllerContext) throws PortletException {

    	Set<String> map = this.repository.getMapFeed(portalControllerContext);    	
        return map; 
    }

    public FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url) throws PortletException {

    	FeedRssModel mod = this.repository.getMapFeed(portalControllerContext, id, name, url);
        return mod; 
    }
    
    /**
     * Modification Feed
     */
	public void modFeed(PortalControllerContext portalControllerContext, FeedRssModel model)
			throws PortletException {
		ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
		List<FeedRssModel> list = new ArrayList<FeedRssModel>();
		list.add(model);
		container.setFeedSources(list);		
		this.repository.modFeed(portalControllerContext, container);
		
	}

    /**
     * delete Feed
     */
	public void delFeed(PortalControllerContext portalControllerContext, FeedRssModel model)
			throws PortletException {
		
		ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
		List<FeedRssModel> list = new ArrayList<FeedRssModel>();
		list.add(model);
		container.setFeedSources(list);		
		
		this.repository.delFeed(portalControllerContext, container);
	}	
	
    /**
     * get Map Container Feed
     */	
    public ContainerRssModel getMapContainer(PortalControllerContext portalControllerContext) throws PortletException {

		ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
		Document doc = this.repository.getCurrentDocument(portalControllerContext);
		container.setPath(doc.getPath());
		container.setName(doc.getTitle());
		container.setDocId(doc.getId());
		
		Set<String> map = this.repositoryContainer.getMapContainer(portalControllerContext);
		container.setMap(map);
		
        return container; 
    }	
    
    public void modifContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException, IOException {
    	this.repositoryContainer.modifContainer(portalControllerContext, model);  	
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, false, model.getPath());    
        response.sendRedirect(redirectionUrl);
    }

    /**
     * Remove container service
     * @throws IOException 
     */
	public void removeContainer(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException, IOException {
		
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        
        String docid = model.getDocId();
    	this.repositoryContainer.remove(portalControllerContext, docid);
    	
        // Redirection URL
        String path = StringUtils.substringBeforeLast(model.getPath(), "/");
    	
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, false, path);        
        response.sendRedirect(redirectionUrl);
		
	} 
	
    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param refresh refresh indicator
     * @return URL
     * @throws PortletException
     */
    private String getRedirectionUrl(PortalControllerContext portalControllerContext, boolean refresh, String path) throws PortletException {
        // Redirection URL
        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
    }	
}
