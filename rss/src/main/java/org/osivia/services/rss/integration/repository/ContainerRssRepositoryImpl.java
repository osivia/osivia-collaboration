package org.osivia.services.rss.integration.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.ContainerRssListNuxeoCommand;
import org.osivia.services.rss.integration.RssCreatNuxeoCommand;
import org.osivia.services.rss.integration.Model.ContainerRssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContainerRssRepositoryImpl implements ContainerRssRepository{

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * Constructor.
     */
    public ContainerRssRepositoryImpl() {
        super();
    }
	
    /**
     * Création du conteneur de flux RSS
     */
    public void creation(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        command = this.applicationContext.getBean(RssCreatNuxeoCommand.class, model);

        nuxeoController.executeNuxeoCommand(command);
    }
    
    /**
     * getList container RSS
     */
    public void getListContainerRss(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand nuxeoCommand = new ContainerRssListNuxeoCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED);
        // Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);

    }

	@Override
	public void remove(PortalControllerContext portalControllerContext, ContainerRssModel model)
			throws PortletException {
		// TODO Auto-generated method stub
		
	}

}
