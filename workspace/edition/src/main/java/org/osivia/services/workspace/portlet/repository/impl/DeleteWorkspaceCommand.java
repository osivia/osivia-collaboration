package org.osivia.services.workspace.portlet.repository.impl;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Delete workspace Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class DeleteWorkspaceCommand implements INuxeoCommand {

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
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.PutDocumentInTrash");
        request.set("document", this.path);
        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
