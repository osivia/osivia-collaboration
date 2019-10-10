package org.osivia.services.rss.integration;

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
import org.osivia.services.rss.integration.Model.ConteneurRssModel;
import org.osivia.services.rss.integration.Model.RssModel;
import org.osivia.services.rss.integration.repository.RssRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * List Nuxeo events command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
public class RssNuxeoCommand  implements INuxeoCommand {
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "rss";
    /** Conteneur Nuxeo document type. */
    private static final String DOCUMENT_TYPE_CONTENEUR = "rss";
    
    /** RSS Model. */
    private RssModel event;

    /** Conteneur RSS Model. */
    private ConteneurRssModel conteneur;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(RssNuxeoCommand.class);
	
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // TODO: Parent --> à voir comment et où il est valorisé
        DocRef parent = new DocRef(this.event.getAuthor());

    	PropertyMap puit = fillConteneur(conteneur);
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
        map.set(RssRepository.ID_GUID_PROPERTY, event.getGuid());
        map.set(RssRepository.CATEGORY_PROPERTY, event.getCategory());
        map.set(RssRepository.ENCLOSURE_PROPERTY, event.getEnclosure());
        map.set(RssRepository.LINK_PROPERTY, event.getLink());
        map.set(RssRepository.SOURCES_PROPERTY, event.getSourceRss());
        map.set(RssRepository.AUTHOR_PROPERTY, event.getAuthor());
        
        return map;
    }
	
	//	On rempli le conteneur de RSS
	private PropertyMap fillConteneur(ConteneurRssModel conteneur) throws JsonGenerationException, JsonMappingException, IOException
    {
        PropertyMap map = new PropertyMap();
        // Valorisation du conteneur RSS
        map.set(RssRepository.TITLE_CONTENEUR_PROPERTY, conteneur.getTitleConteneur());
        map.set(RssRepository.DESCRIPTION_CONTENEUR_PROPERTY, conteneur.getDescriptionConteneur());
        // TODO: Vérifier le stockage et la restitution de la  date 
        map.set(RssRepository.PUBDATE_CONTENEUR_PROPERTY, conteneur.getPubDateConteneur());
        // TODO: Générer un id par défaut dans le setter
        map.set(RssRepository.ID_CONTENEUR_PROPERTY, conteneur.getIdConteneur());
        
        return map;
    }		
}
