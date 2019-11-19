package org.osivia.services.rss.common.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update Container RSS Nuxeo command.
 *
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContainerUpdateCommand implements INuxeoCommand {
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "RssContainer";
    
    /** RSS Model. */
    private ContainerRssModel form;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(ContainerCreatCommand.class);
    
    /**
    * Constructor.
    *
    * @param form ContainerRssModel
    */
   public ContainerUpdateCommand(ContainerRssModel form) {
       super();
       this.form = form;
   }
    
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Path Document
        DocRef parent = new DocRef(this.form.getPath());

        // Feeds sources property
        PropertyMap properties = new PropertyMap();
        properties.set(ContainerRepository.NAME_PROPERTY, this.form.getName());
        
        // Mise à jour du conteneur RSS avec l'url, le nom du flux, l'id de synchronisation
        Document document = documentService.update(parent, properties, true);
        
    	return document;
	}
	@Override
	public String getId() {
		return null;
	}
	
}
