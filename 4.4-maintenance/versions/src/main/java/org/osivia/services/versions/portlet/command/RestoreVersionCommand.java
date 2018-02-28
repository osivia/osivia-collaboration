/**
 * 
 */
package org.osivia.services.versions.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class RestoreVersionCommand implements INuxeoCommand {
    
    /** Id of Nuxeo command. */
    private final static String NX_OPERATION_ID = "Document.RestoreVersion";
    
    /** Version id to restore. */
    private String versionId;
    
    /**
     * Constructor.
     * 
     * @param version
     */
    public RestoreVersionCommand(String versionId){
        this.versionId = versionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document execute(Session nuxeoSession) throws Exception {
        // Get version as Nx document
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        Document verision = documentService.getDocument(versionId);
        
        // Restore version
        OperationRequest request = nuxeoSession.newRequest(NX_OPERATION_ID)
                .setInput(verision);
        return (Document) request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return RestoreVersionCommand.class.getName().concat(" | ").concat(versionId);
    }

}
