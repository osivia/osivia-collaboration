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
public class ListWorkspaceCommand implements INuxeoCommand {
    
    private String sortColumn;
    
    private String sortOrder;
    
    private int pageNumber;
    
    private int pageSize;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     */
    public ListWorkspaceCommand(String sortColumn, String sortOrder, int pageNumber, int pageSize) {
        super();
        this.sortColumn = sortColumn;
        this.sortOrder = sortOrder;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
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
        request.set("query", "SELECT * FROM Workspace WHERE ecm:isVersion = 0 ORDER BY "+sortColumn+ " "+sortOrder);
        request.set("currentPageIndex", pageNumber);
        request.set("pageSize", pageSize);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "PurgeWorkspace "+UUID.randomUUID();
    }

}
