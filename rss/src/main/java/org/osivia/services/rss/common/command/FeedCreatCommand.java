package org.osivia.services.rss.common.command;

import java.util.UUID;

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
 * Create Container RSS Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeedCreatCommand implements INuxeoCommand {
    
    /** RSS Model. */
    private ContainerRssModel form;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(FeedCreatCommand.class);
    
    /**
    * Constructor.
    *
    * @param form ContainerRssModel
    */
   public FeedCreatCommand(ContainerRssModel form) {
       super();
       this.form = form;
   }
    
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Correspond à l'endroit où l'on souhaite stocker le document
//        DocRef parent = new DocRef(form.getPath());

        // A supprimer apres les tests
        DocRef parent = new DocRef("/default-domain/workspaces/frederic");
        
        // Properties
        PropertyMap properties = new PropertyMap();
//         properties.set(ContainerRepository.NAME_PROPERTY, this.form.getName());
        properties.set(ContainerRepository.NAME_PROPERTY, "Toutatice");
        properties.set(ContainerRepository.URL_PROPERTY, this.form.getUrl());
        properties.set(ContainerRepository.ID_PART_PROPERTY, this.form.getPartId());
        getDisplayName();
        getSyncId();
        properties.set(ContainerRepository.ID_PROPERTY, this.form.getSyncId());
        properties.set(ContainerRepository.DISPLAY_NAME_PROPERTY, this.form.getDisplayName());
        
        // Mise à jour du conteneur RSS avec l'url, le nom du flux, la synchronisation
        Document document = documentService.createDocument(parent, ContainerRepository.DOCUMENT_TYPE_CONTENEUR, null, properties);
        
    	return document;
	}
	
    @Override
	public String getId() {
		return null;
	}
    
    public void getSyncId() {
    	this.form.setSyncId(UUID.randomUUID().toString());
    }
		
	public void getDisplayName() {
		int firstComa = this.form.getUrl().indexOf('.')+1;
		int secondComa = this.form.getUrl().indexOf('.', firstComa);
		String displayName = this.form.getUrl().substring(firstComa, secondComa);
		this.form.setDisplayName(displayName);
	}
}
