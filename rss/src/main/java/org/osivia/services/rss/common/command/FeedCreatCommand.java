package org.osivia.services.rss.common.command;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyList;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyMap;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.model.Picture;
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
    private String mode;
    
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(FeedCreatCommand.class);
    
    /**
    * Constructor.
    *
    * @param form ContainerRssModel
    */
   public FeedCreatCommand(ContainerRssModel form, String mode) {
       super();
       this.form = form;
       this.mode = mode;
   }
    
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Path Document
        DocRef parent = new DocRef(this.form.getPath());

        // Feeds sources property
        PropertyMap properties = new PropertyMap();
//        String feedsSources = this.getFeedsSourcesProperty(form.getFeedSources());
        
        Document document = this.form.doc;
        int index = 1;
        if(mode.equalsIgnoreCase("del")) {
            OperationRequest request = nuxeoSession.newRequest("Document.RemoveProperty");
            request.setInput(document);
            request.set("xpath", ContainerRepository.FEEDS_PROPERTY + "/" + index);
            request.execute();
        }
//        String feedsSources = "syncId=jbgfeaz\ndisplayName=Coucou\nurl=kbghf";
        if(mode.equalsIgnoreCase("add")) {
          OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
          String feedSource = getFeed();
          request.setInput(document);
          request.set("xpath", ContainerRepository.FEEDS_PROPERTY);
          request.set("value", feedSource);
          request.execute();
        }
        
		if (mode.equalsIgnoreCase("mod")) {
			String path = "rssc:feeds/" + index + "/" + ContainerRepository.DISPLAY_NAME_PROPERTY;
			String mod = "";
			documentService.setProperty(parent, path, mod);
		}
        // Mise à jour du conteneur RSS avec l'url, le nom du flux, l'id de synchronisation
//        Document document = documentService.update(parent, properties, true);
        
//        prop.get(ContainerRepository.FEEDS_PROPERTY);
//        documentService.setProperty(parent, ContainerRepository.FEEDS_PROPERTY, feedsSources);        
        
        
        // Visual
        Picture visual = this.form.getVisual();
		if (visual != null) {
			
			if (visual.isUpdated()) {
				// Temporary file
				File temporaryFile = visual.getTemporaryFile();

				// File blob
				Blob blob = new FileBlob(temporaryFile);
				blob.setFileName(visual.getName());
				documentService.setBlob(this.form.doc, blob, "rssc:feeds/" + this.form.getFeed().getIndexNuxeo() + "/logos");

				// Delete temporary file
				temporaryFile.delete();
			} else if (visual.isDeleted()) {
				documentService.removeBlob(this.form.doc, "rssc:feeds/" + this.form.getFeed().getIndexNuxeo() + "/logos");
			}
		}
		
    	return document;
	}
	
    @Override
	public String getId() {
		return null;
	}
   
	/**
	 * Get Feed Nuxeo document property.
	 * 
	 * @param feed synchronization sources
	 * @return property
	 */    
    private String getFeed() {
    	String feed = null;
		if(this.form.getFeed().getSyncId() == null) {
			this.form.getFeed().setSyncId(UUID.randomUUID().toString());					
		}
		String id = ContainerRepository.ID_PROPERTY + "=" + this.form.getFeed().getSyncId() + "\n";
		String displayName = ContainerRepository.DISPLAY_NAME_PROPERTY + "=" + this.form.getFeed().getDisplayName() +"\n";
		String url = ContainerRepository.URL_PROPERTY + "=" + this.form.getFeed().getUrl().toString();
		feed = id + displayName + url;

    	return feed;
    }
	/**
	 * Get Feeds Nuxeo document property.
	 * 
	 * @param feeds synchronization sources
	 * @return property
	 */
	private String getFeedsSourcesProperty(List<FeedRssModel> sources) {

		String property;

		if (CollectionUtils.isEmpty(sources)) {
			property = null;
		} else {
			NuxeoPropertyList propertyList = new NuxeoPropertyList(sources.size());
			for (FeedRssModel source : sources) {
				NuxeoPropertyMap propertyMap = new NuxeoPropertyMap();
				if(source.getSyncId() == null) {
					// Identifier
					source.setSyncId(UUID.randomUUID().toString());					
				}
				propertyMap.put(ContainerRepository.ID_PROPERTY, source.getSyncId());
				propertyMap.put(ContainerRepository.DISPLAY_NAME_PROPERTY, source.getDisplayName());

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
