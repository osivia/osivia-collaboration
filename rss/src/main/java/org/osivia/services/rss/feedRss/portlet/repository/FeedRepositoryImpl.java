package org.osivia.services.rss.feedRss.portlet.repository;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, model);

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
        
    	// Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current Nuxeo document
        Document document = this.getCurrentDocument(portalControllerContext);
    	ContainerRssModel container = this.applicationContext.getBean(ContainerRssModel.class);
        container.setDoc(document);
        List<FeedRssModel> feed = null;
        if(document != null) {
            feed = fillFeed(document, nuxeoController);        	
        }

    	container.setFeedSources(feed);
        
        return container;
    }       
    
	private List<FeedRssModel> fillFeed(Document document, NuxeoController nuxeoController) {
		
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
                listFeed.add(feed);
            }        	
        }
	    return listFeed;
	}
	
	private List<FeedRssModel> fillFeed(Document document, NuxeoController nuxeoController, ContainerRssModel model) {
		
        ArrayList<FeedRssModel> listFeed = new ArrayList<FeedRssModel>();
        PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);
        for (FeedRssModel feed : model.getFeedSources()) {
        	listFeed.add(feed);
        }
        if (propertyList != null) {
        	FeedRssModel feedNuxeo;
            for (int i = 0; i < propertyList.size(); i++) {
                PropertyMap map = propertyList.getMap(i);
				feedNuxeo = new FeedRssModel();
				feedNuxeo.setSyncId(map.getString(ID_PROPERTY));
				feedNuxeo.setUrl(map.getString(URL_PROPERTY));
				feedNuxeo.setDisplayName(map.getString(DISPLAY_NAME_PROPERTY));
				listFeed.add(feedNuxeo);
            }        	
        }
	    return listFeed;
	}	

	@Override
	public void remove(PortalControllerContext portalControllerContext) throws PortletException {
		// TODO Auto-generated method stub
		
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
    public FeedRssModel getMapFeed(PortalControllerContext portalControllerContext, String id, String name, String url) throws PortletException {
        
        FeedRssModel model = applicationContext.getBean(FeedRssModel.class);
        
        Map<String, String> mapFeed = new HashMap<String, String>();
        
        // Current Nuxeo document
        Document document = this.getCurrentDocument(portalControllerContext);
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
	public void modFeed(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        Document document = this.getCurrentDocument(portalControllerContext);
        model.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        model.setDoc(document);
        List<FeedRssModel> feed = null;
        if(document != null) {
            feed = fillFeedMod(document, nuxeoController, model);        	
        }
        
		model.setFeedSources(feed);
		// Nuxeo command
		INuxeoCommand nuxeoCommand;
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, model);

		nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // Refresh document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
        documentContext.reload();
        document = documentContext.getDocument();
		
	}

	private List<FeedRssModel> fillFeedMod(Document document, NuxeoController nuxeoController, ContainerRssModel model) {
		
        ArrayList<FeedRssModel> listFeed = new ArrayList<FeedRssModel>();
        PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);
        String url = null;
        String displayName = null;
        String syncId = null;
        for (FeedRssModel feed : model.getFeedSources()) {
        	displayName = feed.getDisplayName();
        	syncId = feed.getSyncId();
        	url = feed.getUrl();
        }
        if (propertyList != null) {
        	FeedRssModel feedNuxeo;
            for (int i = 0; i < propertyList.size(); i++) {
                PropertyMap map = propertyList.getMap(i);
				feedNuxeo = new FeedRssModel();
				if(syncId.equalsIgnoreCase(map.getString(ID_PROPERTY))) {
					feedNuxeo.setUrl(url);
					feedNuxeo.setDisplayName(displayName);
				} else {
					feedNuxeo.setUrl(map.getString(URL_PROPERTY));
					feedNuxeo.setDisplayName(map.getString(DISPLAY_NAME_PROPERTY));					
				}
				feedNuxeo.setSyncId(map.getString(ID_PROPERTY));

				listFeed.add(feedNuxeo);
            }        	
        }
	    return listFeed;
	}	
	
    /**
     * Delete Feed.
     */
	public void delFeed(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        Document document = this.getCurrentDocument(portalControllerContext);
        model.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        model.setDoc(document);
        List<FeedRssModel> feed = null;
        String syncId = null;
        for (FeedRssModel feed1 : model.getFeedSources()) {
        	syncId = feed1.getSyncId();
        }
        if(document != null) {
            feed = delFeed(document, nuxeoController, model, syncId);        	
        }
        
		model.setFeedSources(feed);
		// Nuxeo command
		INuxeoCommand nuxeoCommand;
		nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, model);

		nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // Refresh document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
        documentContext.reload();
        document = documentContext.getDocument();
        
		// Nuxeo command
		// Read doc RssItems with syncId
		nuxeoCommand = this.applicationContext.getBean(ItemListCommand.class, syncId);

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
    
	private List<FeedRssModel> delFeed(Document document, NuxeoController nuxeoController, ContainerRssModel model, String syncId) {
		
        ArrayList<FeedRssModel> listFeed = new ArrayList<FeedRssModel>();
        PropertyList propertyList = (PropertyList) document.getProperties().get(FEEDS_PROPERTY);

        if (propertyList != null) {
        	FeedRssModel feedNuxeo;
            for (int i = 0; i < propertyList.size(); i++) {
                PropertyMap map = propertyList.getMap(i);
				feedNuxeo = new FeedRssModel();
				if(!syncId.equalsIgnoreCase(map.getString(ID_PROPERTY))) {
					feedNuxeo.setUrl(map.getString(URL_PROPERTY));
					feedNuxeo.setDisplayName(map.getString(DISPLAY_NAME_PROPERTY));					
					feedNuxeo.setSyncId(map.getString(ID_PROPERTY));
					listFeed.add(feedNuxeo);
				} 
            }        	
        }
	    return listFeed;
	}		
		
}
