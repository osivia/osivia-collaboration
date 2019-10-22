package org.osivia.services.rss.container.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.container.portlet.command.ContainerCreatNuxeoCommand;
import org.osivia.services.rss.container.portlet.command.ContainerRssListNuxeoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 */
@Repository
public class ContainerRepositoryImpl implements ContainerRepository{

    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;
    
    /**
     * Constructor.
     */
    public ContainerRepositoryImpl() {
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
        INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ContainerRssListNuxeoCommand.class);
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

	    ContainerRssModel container = this.applicationContext.getBean(ContainerRssModel.class);
	    container.setDisplayName(displayName);
	    container.setUrl(url);
	    container.setPartId(partId);
	    container.setSyncId(syncId);
	    
	    return container;
	}    
    
    /**
     * Create container RSS
     */    
	public void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand nuxeoCommand;
        nuxeoCommand = this.applicationContext.getBean(ContainerCreatNuxeoCommand.class, model);
        
        nuxeoController.executeNuxeoCommand(nuxeoCommand);
		
	}

	@Override
	public void remove(PortalControllerContext portalControllerContext) throws PortletException {
		// TODO Auto-generated method stub
		
	}

}
