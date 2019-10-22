package org.osivia.services.rss.feedRss.portlet.command;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.container.portlet.repository.ContainerRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Create Container RSS Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
public class CreatFeedNuxeoCommand implements INuxeoCommand {
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "RssContainer";
    
    /** RSS Model. */
    private ContainerRssModel container;

	/** logger */
    protected static final Log logger = LogFactory.getLog(CreatFeedNuxeoCommand.class);
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        DocRef parent = new DocRef("");

    	PropertyMap flux = fillRss(container);

        // Création du document RSS
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE_RSS, null, flux, true);
        
    	return document;
	}
	@Override
	public String getId() {
		return null;
	}
	
	//	On rempli le flux RSS
	private PropertyMap fillRss(ContainerRssModel container) throws JsonGenerationException, JsonMappingException, IOException
    {
        PropertyMap map = new PropertyMap();
        
        // Valorisation du schéma RSS (Item)
        map.set(ContainerRepository.ID_PART_PROPERTY, container.getPartId());
        map.set(ContainerRepository.DISPLAY_NAME_PROPERTY, container.getDisplayName());
        map.set(ContainerRepository.ID_PROPERTY, container.getSyncId());
        map.set(ContainerRepository.URL_PROPERTY, container.getUrl());
        
        return map;
    }
			
}
