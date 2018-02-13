package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command;

import java.util.UUID;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoCompatibility;

/**
 * List Workspace command.
 *
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class ListDeletedWorkspaceCommand implements INuxeoCommand {

    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     */
    public ListDeletedWorkspaceCommand() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

    	// Request
        OperationRequest request;
        if (NuxeoCompatibility.canUseES()) {
            request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice");
        } else {
            request = nuxeoSession.newRequest("Document.Query");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice");
        }
        request.set("query", "SELECT * FROM Workspace WHERE ecm:isVersion = 0 AND ecm:currentLifeCycleState = 'deleted' ");

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "PurgeWorkspace"+UUID.randomUUID();
    }

}
