package org.osivia.services.workspace.quota.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get trashed documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateQuotaCommand implements INuxeoCommand {

    /** User Id */
    private final String userId;


    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public UpdateQuotaCommand(String userId) {
        super();
        this.userId = userId;
    }

    public Document getUserProfile(Session automationSession, String userId) throws Exception {
     	
        OperationRequest newRequest = automationSession.newRequest("Services.GetToutaticeUserProfile");
        newRequest.set("username", userId);

        Document refDoc = (Document) newRequest.execute();

        Document doc = (Document) automationSession.newRequest("Document.FetchLiveDocument").setHeader(Constants.HEADER_NX_SCHEMAS, "*").set("value", refDoc)
                .execute();

        return doc;

    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Espace perso
/*    	
        Document userProfile = getUserProfile(nuxeoSession,  userId);
        String workspacePath = userProfile.getPath(); 
        workspacePath = workspacePath.substring(0, workspacePath.lastIndexOf('/'));
*/       
        // TODO : check not null
    	String workspacePath = "/default-domain/workspaces/espace-11";

        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        Document docRoot = documentService.getDocument(new PathRef(workspacePath)); 

        documentService.setProperty(docRoot, "qt:maxSize", "250000");

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.userId);
        return builder.toString();
    }

}
