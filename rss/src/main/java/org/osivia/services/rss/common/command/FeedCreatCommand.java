package org.osivia.services.rss.common.command;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyList;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyMap;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.repository.ContainerRepository;
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
public class FeedCreatCommand implements INuxeoCommand {
    
    /** Container RSS Model. */
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

        // Path Document
        DocRef parent = new DocRef(this.form.getPath());

        // Feeds sources property
        PropertyMap properties = new PropertyMap();
        String feedsSources = this.getFeedsSourcesProperty(form.getFeedSources());
        properties.set(ContainerRepository.FEEDS_PROPERTY, feedsSources);
        
        // Mise à jour du conteneur RSS avec l'url, le nom du flux, l'id de synchronisation
        Document document = documentService.update(parent, properties, true);
        
    	return document;
	}
	
    @Override
	public String getId() {
		return null;
	}
    
	/**
	 * Get Feed Nuxeo document property.
	 * 
	 * @param feeds synchronization sources
	 * @return property
	 */
	private String getFeedsSourcesProperty(List<FeedRssModel> sources) {
		NuxeoPropertyList propertyList = new NuxeoPropertyList(sources.size());
		NuxeoPropertyMap propertyMap = new NuxeoPropertyMap();

		String property;

		if (CollectionUtils.isEmpty(sources)) {
			property = null;
		} else {

			for (FeedRssModel source : sources) {

				// Identifier
				source.setSyncId(UUID.randomUUID().toString());
				propertyMap.put(ContainerRepository.ID_PROPERTY, source.getSyncId());

				// Display Name
				int firstComa = source.getUrl().toString().indexOf('.') + 1;
				int secondComa = source.getUrl().toString().indexOf('.', firstComa);
				String displayName = source.getUrl().toString().substring(firstComa, secondComa);

				propertyMap.put(ContainerRepository.DISPLAY_NAME_PROPERTY, displayName);

				// URL
				String url = source.getUrl().toString();
				propertyMap.put(ContainerRepository.URL_PROPERTY, url);
				
				propertyList.add(propertyMap);
			}
			property = propertyList.toString();
		}

		return property;

	}
}
