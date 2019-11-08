package org.osivia.services.rss.feedRss.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.ItemCreatCommand;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void creatItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items)
			throws PortletException {
		
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;

//        for (ItemRssModel item : items) {
//	        command = this.applicationContext.getBean(ItemCreatCommand.class, item);
//	        nuxeoController.executeNuxeoCommand(command);
//        }
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
