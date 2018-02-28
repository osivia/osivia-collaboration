package org.osivia.services.workspace.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Delete workspace Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteWorkspaceCommand implements INuxeoCommand {

    /** Operation identifier . */
    private static final String OPERATION_ID = "Document.PutDocumentInTrash";


    /** Workspace path. */
    private final String path;


    /**
     * Constructor.
     * 
     * @param path workspace path
     */
    public DeleteWorkspaceCommand(String path) {
        super();
        this.path = path;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document reference
        DocRef docRef = new DocRef(this.path);
        
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_ID);
        request.set("document", docRef);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(this.path);
        return builder.toString();
    }

}
