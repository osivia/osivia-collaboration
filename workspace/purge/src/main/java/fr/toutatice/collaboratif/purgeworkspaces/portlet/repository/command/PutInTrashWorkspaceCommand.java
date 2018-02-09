package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Put in trash command
 * @author Julien Barberet
 */
public class PutInTrashWorkspaceCommand implements INuxeoCommand {
    
	/** Id of the workspace to put in trash */
    private String docId;

    /**
     * Constructor
     * @param docId
     */
    public PutInTrashWorkspaceCommand(String docId) {
        this.docId = docId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        return nuxeoSession.newRequest("Document.PutDocumentInTrash").set("document", docId).setHeader("nx_es_sync", "true").execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Document.PutInTrash: " + docId;
    }

}
