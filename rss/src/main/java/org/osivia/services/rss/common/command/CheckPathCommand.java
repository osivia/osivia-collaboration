package org.osivia.services.rss.common.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * CheckPath Nuxeo command.
 *
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckPathCommand implements INuxeoCommand {

    /**
     * Base path proxy.
     */
    private final String proxyBasePath;
    
    /**
     * Name.
     */
    private final String name;
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "RssContainer";    
    
    public CheckPathCommand(String proxyBasePath, String name) {
		super();
		this.proxyBasePath = proxyBasePath;
		this.name = name;
	}

    @Override
    public Object execute(Session nuxeoSession)  {
    	
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        DocRef parent = new DocRef(this.proxyBasePath);

        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set(ContainerRepository.NAME_PROPERTY, this.name);
        
        // Création du document RSS
        Boolean error = false; 
		try {
			documentService.createDocument(parent, DOCUMENT_TYPE_RSS, null, properties);
		} catch (Exception e) {
			error = true;
		}
        
    	return error;
    	
    }

    @Override
    public String getId() {
        return null;
    }
}
