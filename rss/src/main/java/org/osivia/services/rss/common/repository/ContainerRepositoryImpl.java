package org.osivia.services.rss.common.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.ContainerCreatCommand;
import org.osivia.services.rss.common.command.ContainerListCommand;
import org.osivia.services.rss.common.command.ContainerRemoveCommand;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
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
        INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ContainerListCommand.class);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
        containers = new ArrayList<ContainerRssModel>(documents.size());
        
        for (Document document : documents) {
        	ContainerRssModel container = fillContainer(document, nuxeoController);
        	containers.add(container);
        }        
        
		return containers;
    }
    
	private ContainerRssModel fillContainer(Document document, NuxeoController nuxeoController) {
	    String name = document.getString(NAME_PROPERTY);
	    
	    ContainerRssModel container = this.applicationContext.getBean(ContainerRssModel.class);
	    container.setName(name);
	    DocumentDTO dto = DocumentDAO.getInstance().toDTO(document);
		container.setDocument(dto);
	    return container;
	}
    
    
    /**
     * Create Container RSS
     */    
	public void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand nuxeoCommand;
        nuxeoCommand = this.applicationContext.getBean(ContainerCreatCommand.class, model);
        
        nuxeoController.executeNuxeoCommand(nuxeoCommand);
	}
	
    /**
     * getMap container RSS
     */
    public Map<Integer, String> getMapContainer(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        Map<Integer, String> map = new HashMap<Integer, String>();
        
        // Nuxeo command
        INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ContainerListCommand.class);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
        
        int i = 0;
        for (Document document : documents) {
        	map.put(i, document.getString(NAME_PROPERTY));
        	i++;
        }
        
		return map;
    }	

    /**
     * Remove container RSS
     */
	public void remove(PortalControllerContext portalControllerContext, String docid) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ContainerRemoveCommand.class, docid);
        nuxeoController.executeNuxeoCommand(nuxeoCommand);
        
	}
}
