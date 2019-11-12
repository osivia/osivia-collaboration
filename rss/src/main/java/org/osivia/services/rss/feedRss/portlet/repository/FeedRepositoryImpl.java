package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.FeedCreatCommand;
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
        
        model.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath());
        // Nuxeo command
        INuxeoCommand nuxeoCommand;
        nuxeoCommand = this.applicationContext.getBean(FeedCreatCommand.class, model);
        
        nuxeoController.executeNuxeoCommand(nuxeoCommand);
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
                feed.setSyncId(map.getString(DISPLAY_NAME_PROPERTY));
                listFeed.add(feed);
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
	
}
