package org.osivia.services.rss.common.command;

import java.io.File;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.osivia.services.rss.common.model.ContainerRssModel;
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

        Document document = this.form.doc;
        if(mode.equalsIgnoreCase("del")) {
            OperationRequest request = nuxeoSession.newRequest("Document.RemoveProperty");
            request.setInput(document);
            request.set("xpath", ContainerRepository.FEEDS_PROPERTY + "/" + this.form.getFeed().getIndexNuxeo());
            request.execute();
        }
        if(mode.equalsIgnoreCase("add")) {
          OperationRequest request = nuxeoSession.newRequest("Document.AddComplexProperty");
          String feedSource = setFeed();
          request.setInput(document);
          request.set("xpath", ContainerRepository.FEEDS_PROPERTY);
          request.set("value", feedSource);
          request.execute();
        }
        
		if (mode.equalsIgnoreCase("mod")) {
			String path = ContainerRepository.FEEDS_PROPERTY + "/" + this.form.getFeed().getIndexNuxeo() + "/" + ContainerRepository.DISPLAY_NAME_PROPERTY;
			documentService.setProperty(parent, path, this.form.getFeed().getDisplayName());
		}
        
        // Visual
        Picture visual = this.form.getVisual();
		if (visual != null) {
			
			if (visual.isUpdated()) {
				// Temporary file
				File temporaryFile = visual.getTemporaryFile();

				// File blob
				Blob blob = new FileBlob(temporaryFile);
				blob.setFileName(visual.getName());
				documentService.setBlob(this.form.doc, blob, ContainerRepository.FEEDS_PROPERTY + "/" + this.form.getFeed().getIndexNuxeo() + "/" + ContainerRepository.LOGO_PROPERTY);

				// Delete temporary file
				temporaryFile.delete();
			} else if (visual.isDeleted()) {
				documentService.removeBlob(this.form.doc, ContainerRepository.FEEDS_PROPERTY + "/" + this.form.getFeed().getIndexNuxeo() + "/" + ContainerRepository.LOGO_PROPERTY);
			}
		}
		
    	return document;
	}
	
    @Override
	public String getId() {
		return null;
	}
   
	/**
	 * setFeed Nuxeo document property.
	 * 
	 * @param feed synchronization sources
	 * @return property
	 */    
    private String setFeed() {
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
}
