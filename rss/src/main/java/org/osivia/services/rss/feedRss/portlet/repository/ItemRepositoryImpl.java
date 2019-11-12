package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.ItemCreatCommand;
import org.osivia.services.rss.common.command.ItemListCommand;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemRepositoryImpl implements ItemRepository{

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Constructor.
     */
    public ItemRepositoryImpl() {
        super();
    }
	
    /**
     * Create Item RSS
     */
    public void creatItem(PortalControllerContext portalControllerContext, ItemRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        command = this.applicationContext.getBean(ItemCreatCommand.class, model);

        nuxeoController.executeNuxeoCommand(command);
    }

	@Override
	public void removeItem(PortalControllerContext portalControllerContext) throws PortletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ItemRssModel> getListItemRss(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        List<ItemRssModel> items;
        
        // Nuxeo command
        INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ItemListCommand.class);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
        items = new ArrayList<ItemRssModel>(documents.size());
        
        for (Document document : documents) {
        	// Faire un test sur le path pour faire correspond avec le conteneur
        	ItemRssModel item = fillItem(document, nuxeoController);
        	items.add(item);
        }        
        
		return items;
	}

	private ItemRssModel fillItem(Document document, NuxeoController nuxeoController) {
	    String id = document.getString(CONTENEUR_PROPERTY);
	    String title = document.getString(TITLE_PROPERTY);	    
	    String link = document.getString(LINK_PROPERTY);
	    String description = document.getString(DESCRIPTION_PROPERTY);
	    String autor = document.getString(AUTHOR_PROPERTY);
	    String category = document.getString(CATEGORY_PROPERTY);
	    String enclosure = document.getString(ENCLOSURE_PROPERTY);
	    String pubdate =  document.getString(PUBDATE_PROPERTY);
	    String guid =  document.getString(GUID_PROPERTY);
	    String sources =  document.getString(SOURCES_PROPERTY);
	    
	    ItemRssModel item = new ItemRssModel();
	    item.setIdConteneur(id);
	    item.setTitle(title);
	    item.setLink(link);
	    item.setDescription(description);
	    item.setAuthor(autor);
	    item.setCategory(category);
	    item.setEnclosure(enclosure);
	    item.setPubDate(pubdate);
	    item.setGuid(guid);
	    item.setSourceRss(sources);
	    return item;
	}
	
	@Override
	public void creatItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items)
			throws PortletException {
		
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;

        for (ItemRssModel item : items) {
        	item.setPath(nuxeoController.getCurrentDocumentContext().getCmsPath()); 
        	command = this.applicationContext.getBean(ItemCreatCommand.class, item);
	        nuxeoController.executeNuxeoCommand(command);
        }
	}

	@Override
	public void removeItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items)
			throws PortletException {
		
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        
//		for (ItemRssModel item : items) {
//	        command = this.applicationContext.getBean(ItemRemoveCommand.class, item);
//	        nuxeoController.executeNuxeoCommand(command);
//		}
		
	}
    
}
