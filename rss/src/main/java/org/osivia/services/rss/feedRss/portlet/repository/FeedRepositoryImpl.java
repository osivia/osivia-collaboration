package org.osivia.services.rss.feedRss.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.container.portlet.command.ContainerCreatNuxeoCommand;
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
 * @author Cédric Krommenhoek
 * @see AbstractCalendarCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeedRepositoryImpl {

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
     * Création des flux RSS
     */
    public void creation(PortalControllerContext portalControllerContext, ItemRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        command = this.applicationContext.getBean(ContainerCreatNuxeoCommand.class, model);

        nuxeoController.executeNuxeoCommand(command);
    }
    
}
