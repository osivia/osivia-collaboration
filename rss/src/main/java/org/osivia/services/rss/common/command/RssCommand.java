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

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Liste des flux RSS events command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
public class RssCommand  implements INuxeoCommand {
    
    
    /** Rss Nuxeo document type. */
    private static final String DOCUMENT_TYPE_RSS = "RssContainer";
    
    /** RSS Model. */
    private ContainerRssModel form;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(ContainerCreatNuxeoCommand.class);	
	
    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     */
    public RssCommand(NuxeoQueryFilterContext queryContext, String contextPath) {
        super();
        this.form = form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Correspond à l'endroit où l'on souhaite stocker le document
        DocRef parent = new DocRef(form.getPath());

        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set(ContainerRepository.DISPLAY_NAME_PROPERTY, this.form.getDisplayName());
        properties.set(ContainerRepository.ID_PART_PROPERTY, this.form.getPartId());
        properties.set(ContainerRepository.ID_PROPERTY, this.form.getSyncId());
        properties.set(ContainerRepository.NAME_PROPERTY, this.form.getName());
        properties.set(ContainerRepository.URL_PROPERTY, this.form.getUrl());
        
        
        // Création du document RSS
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE_RSS, null, properties);
        
    	return document;
    }


	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
