package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Purge command
 * @author Julien Barberet
 */
public class PurgeCommand implements INuxeoCommand {
    
	/** List of id of workspaces to purge */
    private List<String> listIdToPurge;

    /**
     * Constructor
     * @param listIdToPurge list of id of workspaces to purge
     */
    public PurgeCommand(List<String> listIdToPurge) {
        this.listIdToPurge = listIdToPurge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        // Inputs
        DocRefs references = new DocRefs(this.listIdToPurge.size());
        for (String uid : listIdToPurge)
        {
	        DocRef reference = new DocRef(uid);
	        references.add(reference);
        }
        
        return nuxeoSession.newRequest("Services.PurgeDocuments").setInput(references).setHeader("nx_es_sync", "true").execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Services.PurgeDocuments: " + listIdToPurge.toString();
    }

}
