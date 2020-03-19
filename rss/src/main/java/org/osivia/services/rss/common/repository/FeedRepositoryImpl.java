package org.osivia.services.rss.common.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.FeedCreatCommand;
import org.osivia.services.rss.common.command.ItemListCommand;
import org.osivia.services.rss.common.command.ItemsDeleteCommand;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 */
@Repository
public class FeedRepositoryImpl implements FeedRepository{

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Constructor.
     */
    public FeedRepositoryImpl() {
        super();
    }
	
    /**
     * Create Feed RSS
     */ 
	public void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        Document document = this.getCurrentDocument(portalControllerContext);
        model.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        model.setDoc(document);
        List<FeedRssModel> feed = null;
        if(document != null) {
            feed = fillFeed(document, nuxeoController, model);        	
        }
        
		model.setFeedSources(feed);
		
		// Nuxeo command
		INuxeoCommand nuxeoCommand;
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, model, "add");

		nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // Refresh document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
        documentContext.reload();
        document = documentContext.getDocument();
	}
    
    /**
     * getList feed RSS
     */
    public ContainerRssModel getListFeedRss(PortalControllerContext portalControllerContext) throws PortletException {
        
        // Current Nuxeo document
        Document document = this.getCurrentDocument(portalControllerContext);
    	ContainerRssModel container = this.applicationContext.getBean(ContainerRssModel.class);
        container.setDoc(document);
        List<FeedRssModel> feed = null;
        if(document != null) {
            feed = fillFeed(document);        	
        }

    	container.setFeedSources(feed);
        
        return container;
    }       
    
	public List<FeedRssModel> fillFeed(Document document) {
		
        ArrayList<FeedRssModel> listFeed = new ArrayList<FeedRssModel>();
        PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);
        if (propertyList != null) {
        	FeedRssModel feed;
            for (int i = 0; i < propertyList.size(); i++) {
                PropertyMap map = propertyList.getMap(i);
                feed = new FeedRssModel();
                feed.setSyncId(map.getString(ID_PROPERTY));
                feed.setUrl(map.getString(URL_PROPERTY));
                feed.setDisplayName(map.getString(DISPLAY_NAME_PROPERTY));
                feed.setIndexNuxeo(i);
                listFeed.add(feed);
            }        	
        }
	    return listFeed;
	}
	
	private List<FeedRssModel> fillFeed(Document document, NuxeoController nuxeoController, ContainerRssModel model) {
		
        ArrayList<FeedRssModel> listFeed = new ArrayList<FeedRssModel>();
        PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);

        // Add new feed
        for (FeedRssModel feed : model.getFeedSources()) {
        	model.setVisual(feed.getVisual());
        	feed.getVisual().setName(feed.getSyncId());
        	feed.setIndexNuxeo(propertyList.size());
        	model.setFeed(feed);
        	listFeed.add(feed);
        }
        
	    return listFeed;
	}	
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo document context	
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();

        // Nuxeo document
        Document document;
        if (documentContext == null) {
            document = null;
        } else {
            document = documentContext.getDocument();
        }

        return document;
    }
    
    /**
     * getMap feed RSS
     */
    public Map<String, String> getMapFeed(PortalControllerContext portalControllerContext) throws PortletException {
        
        Map<String, String> mapFeed = new HashMap<String, String>();
        
        // Current Nuxeo document
        Document document = this.getCurrentDocument(portalControllerContext);
        if(document != null) {
            PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);
            if (propertyList != null) {
                for (int i = 0; i < propertyList.size(); i++) {
                    PropertyMap map = propertyList.getMap(i);
                    mapFeed.put(map.getString(DISPLAY_NAME_PROPERTY), map.getString(URL_PROPERTY));
                }        	
            }     	
        }
        
        return mapFeed;
    }

    /**
     * getMap feed RSS
     */
    public FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url, int index) throws PortletException {
        
        FeedRssModel model = applicationContext.getBean(FeedRssModel.class);
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        Map<String, String> mapFeed = new HashMap<String, String>();
        
        // Current Nuxeo document
        Document document = this.getCurrentDocument(portalControllerContext);
        nuxeoController.setCurrentDoc(document);
        boolean init = false;
        if(model.getSyncId() == null) {
        	init = true;
        }
        if(document != null) {
            PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);
            if (propertyList != null) {
				for (int i = 0; i < propertyList.size(); i++) {
					PropertyMap map = propertyList.getMap(i);
					mapFeed.put(map.getString(DISPLAY_NAME_PROPERTY), map.getString(URL_PROPERTY));
					if (init && id.equalsIgnoreCase(map.getString(ID_PROPERTY))) {
						model.setDisplayName(map.getString(DISPLAY_NAME_PROPERTY));
						model.setSyncId(id);
						model.setUrl(map.getString(URL_PROPERTY));
						Picture visual = applicationContext.getBean(Picture.class);
						visual.setUrl(nuxeoController.createFileLink(document, FEEDS_PROPERTY + "/" + i + "/" + LOGO_PROPERTY));
						model.setVisual(visual);
						model.setIndexNuxeo(index);
					}
                }
            }     	
        }
        
        // When Edition feed and only modification of the name you don't have to control the url
        if(name != null && url == null) {
        	mapFeed = null;
        } else {
            model.setMap(mapFeed); 	
        }

        return model;
    }
    
    /**
     * Modification Feed.
     */
	public void modFeed(PortalControllerContext portalControllerContext, FeedRssModel model)
			throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
        Document document = this.getCurrentDocument(portalControllerContext);
        container.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        container.setDoc(document);
        container.setFeed(model);
        container.setVisual(model.getVisual());
        
		// Nuxeo command
		INuxeoCommand nuxeoCommand;
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, container, "mod");

		nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // Refresh document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
        documentContext.reload();
        document = documentContext.getDocument();
		
	}

    /**
     * Delete Feed.
     */
	public void delFeed(PortalControllerContext portalControllerContext, FeedRssModel model)
			throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
        Document document = this.getCurrentDocument(portalControllerContext);
        container.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        container.setDoc(document);
        container.setFeed(model);
        container.setVisual(model.getVisual());
        
		// Nuxeo command
		INuxeoCommand nuxeoCommand;
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, container, "del");

		nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // Refresh document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
        documentContext.reload();
        document = documentContext.getDocument();
        
		// Nuxeo command
		// Read doc RssItems with syncId
		nuxeoCommand = this.applicationContext.getBean(ItemListCommand.class, model.getSyncId());

        List<String> items;
		
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
        items = new ArrayList<String>(documents.size());
        
        for (Document doc : documents) {
        	items.add(doc.getId());
        }
        
        // Delete Items with syncId
		nuxeoCommand = this.applicationContext.getBean(ItemsDeleteCommand.class, items);
		nuxeoController.executeNuxeoCommand(nuxeoCommand);
	}     
    
}
