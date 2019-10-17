package org.osivia.services.rss.container.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.container.portlet.command.ContainerRssListNuxeoCommand;
import org.osivia.services.rss.container.portlet.command.RssCreatNuxeoCommand;
import org.osivia.services.rss.container.portlet.model.ContainerRssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 */
@Repository
public class ContainerRssRepositoryImpl implements ContainerRssRepository{

    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;
    
    /**
     * Constructor.
     */
    public ContainerRssRepositoryImpl() {
        super();
    }
	
    /**
     * getList container RSS
     */
    public List<ContainerRssModel> getListContainerRss(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        List<ContainerRssModel> containers;        
        
        // Nuxeo command
        INuxeoCommand nuxeoCommand = new ContainerRssListNuxeoCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
        // containers
        containers = new ArrayList<ContainerRssModel>(documents.size());

        for (Document document : documents) {
            	ContainerRssModel container = fillContainer(document, nuxeoController);
                containers.add(container);
        }
		return containers;
    }    

	private ContainerRssModel fillContainer(Document document, NuxeoController nuxeoController) {
	    String displayName = document.getString(DISPLAY_NAME_PROPERTY);
	    String url = document.getString(URL_PROPERTY);
	    String partId = document.getString(ID_PART_PROPERTY);
	    String syncId = document.getString(ID_PROPERTY);

	    ContainerRssModel container = this.applicationContext.getBean(ContainerRssModel.class, displayName, url, partId, syncId);
	    
	    return container;
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
    
	@Override
	public ContainerRssModel remove(PortalControllerContext portalControllerContext) throws PortletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContainerRssModel creation(PortalControllerContext portalControllerContext) throws PortletException {
		// TODO Auto-generated method stub
		return null;
	}

}
