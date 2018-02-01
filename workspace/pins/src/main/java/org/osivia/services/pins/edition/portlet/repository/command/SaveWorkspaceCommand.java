package org.osivia.services.pins.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class SaveWorkspaceCommand implements INuxeoCommand {

	private PropertyMap propertyMap;
	
	private Document workspace;
	
    public SaveWorkspaceCommand(Document workspace, PropertyMap propertyMap) {
		super();
		this.workspace = workspace;
		this.propertyMap = propertyMap;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
    	// Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        DocRef docRef = new DocRef(workspace.getId());

        // Update
        workspace = documentService.update(docRef, propertyMap, false);
        
        return workspace;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Pins/" + (this.workspace == null? "" : this.workspace.getPath());
    }
}
