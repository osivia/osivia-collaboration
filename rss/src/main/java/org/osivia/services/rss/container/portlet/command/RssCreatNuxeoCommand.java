package org.osivia.services.rss.container.portlet.command;

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
import org.osivia.services.rss.container.portlet.model.RssModel;
import org.osivia.services.rss.container.portlet.repository.RssRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Create Rss Nuxeo events command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
public class RssCreatNuxeoCommand  implements INuxeoCommand {
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "rss";
    
    /** RSS Model. */
    private RssModel event;

	/** logger */
    protected static final Log logger = LogFactory.getLog(RssCreatNuxeoCommand.class);
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // TODO: Parent --> à voir comment et où il est valorisé
        DocRef parent = new DocRef(this.event.getIdConteneur());

    	PropertyMap flux = fillRss(event);

        // Création du document RSS
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE_RSS, null, flux, true);
        
    	return document;
	}
	@Override
	public String getId() {
		return null;
	}
	
	//	On rempli le flux RSS
	private PropertyMap fillRss(RssModel event) throws JsonGenerationException, JsonMappingException, IOException
    {
        PropertyMap map = new PropertyMap();
        
        // Valorisation du schéma RSS (Item)
        map.set(RssRepository.CONTENEUR_PROPERTY, event.getIdConteneur());
        map.set(RssRepository.TITLE_PROPERTY, event.getTitle());
        map.set(RssRepository.DESCRIPTION_PROPERTY, event.getDescription());
        map.set(RssRepository.PUBDATE_PROPERTY, event.getPubDate());
//        map.set(RssRepository.ID_GUID_PROPERTY, event.getGuid());
        map.set(RssRepository.CATEGORY_PROPERTY, event.getCategory());
        map.set(RssRepository.ENCLOSURE_PROPERTY, event.getEnclosure());
        map.set(RssRepository.LINK_PROPERTY, event.getLink());
        map.set(RssRepository.SOURCES_PROPERTY, event.getSourceRss());
        map.set(RssRepository.AUTHOR_PROPERTY, event.getAuthor());
        
        return map;
    }
			
}
