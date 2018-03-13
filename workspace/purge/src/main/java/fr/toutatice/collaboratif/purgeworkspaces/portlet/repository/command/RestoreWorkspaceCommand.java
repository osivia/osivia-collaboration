package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Restore workspace command
 * @author Julien Barberet
 */
public class RestoreWorkspaceCommand implements INuxeoCommand {
    
	/** Id of the workspace to restore */
    private String uid;

    /**
     * Constructor
     * @param uid Id of the workspace to restore
     */
    public RestoreWorkspaceCommand(String uid) {
        this.uid = uid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        // Inputs
        DocRefs references = new DocRefs(1);
        DocRef reference = new DocRef(uid);
        references.add(reference);
        
        return nuxeoSession.newRequest("Services.RestoreDocuments").setInput(references).setHeader("nx_es_sync", "true").execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Services.RestoreDocuments: " + uid;
    }

}
