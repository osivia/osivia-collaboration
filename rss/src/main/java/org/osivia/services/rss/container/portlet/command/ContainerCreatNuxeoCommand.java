package org.osivia.services.rss.container.portlet.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.container.portlet.model.ContainerRssModel;
import org.osivia.services.rss.container.portlet.repository.ContainerRepository;
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
public class ContainerCreatNuxeoCommand implements INuxeoCommand {
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "RssContainer";
    
    /** RSS Model. */
    private ContainerRssModel form;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(ContainerCreatNuxeoCommand.class);
    
    /**
    * Constructor.
    *
    * @param form ContainerRssModel
    */
   public ContainerCreatNuxeoCommand(ContainerRssModel form) {
       super();
       this.form = form;
   }
    
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Correspond à l'endroit où l'on souhaite stocker le document
        DocRef parent = new DocRef(form.getPath());

        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set(ContainerRepository.NAME_PROPERTY, this.form.getName());
        
        // Création du document RSS
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE_RSS, null, properties);
        
    	return document;
	}
	@Override
	public String getId() {
		return null;
	}
	
}
