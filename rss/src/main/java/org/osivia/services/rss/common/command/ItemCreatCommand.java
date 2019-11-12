package org.osivia.services.rss.common.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.osivia.services.rss.feedRss.portlet.repository.ItemRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Create Container RSS Nuxeo command.
 *
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemCreatCommand implements INuxeoCommand {
    
    /** RSS Model. */
    private ItemRssModel form;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(ItemCreatCommand.class);
    
    /**
    * Constructor.
    *
    * @param form ItemRssModel
    */
   public ItemCreatCommand(ItemRssModel form) {
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
        properties.set(ItemRepository.AUTHOR_PROPERTY, this.form.getAuthor());
        properties.set(ItemRepository.CATEGORY_PROPERTY, this.form.getCategory());
        properties.set(ItemRepository.DESCRIPTION_PROPERTY, this.form.getDescription());
        properties.set(ItemRepository.ENCLOSURE_PROPERTY, this.form.getEnclosure());
        properties.set(ItemRepository.GUID_PROPERTY, this.form.getGuid());
        properties.set(ItemRepository.LINK_PROPERTY, this.form.getLink());
        properties.set(ItemRepository.PUBDATE_PROPERTY, this.form.getPubDate());
        properties.set(ItemRepository.SOURCES_PROPERTY, this.form.getSourceRss());
        properties.set(ItemRepository.TITLE_PROPERTY, this.form.getTitle());        
        
        // Mise à jour du conteneur RSS avec l'url, le nom du flux, la synchronisation
        Document document = documentService.createDocument(parent, ItemRepository.DOCUMENT_TYPE_EVENEMENT, null, properties);
        
    	return document;
	}
	
    @Override
	public String getId() {
		return null;
	}
    
}
