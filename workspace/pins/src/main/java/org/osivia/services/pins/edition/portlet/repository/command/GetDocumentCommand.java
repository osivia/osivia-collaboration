package org.osivia.services.pins.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoCompatibility;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get document from webid information
 * @author jbarberet
 *
 */
public class GetDocumentCommand  implements INuxeoCommand {
    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;
    /** Workspace Uid */
    private String webid;
    
    private String workspacePath;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public GetDocumentCommand(NuxeoQueryFilterContext queryContext, String workspacePath, String webid) {
        super();
        this.queryContext = queryContext;
        this.workspacePath = workspacePath;
        this.webid = webid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:path startswith '").append(workspacePath).append("' ");
      	clause.append(" and ttc:webid ='").append(webid).append("' ");


        // Filter on published documents
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(this.queryContext, clause.toString());

        // Request
        OperationRequest request;
        if (NuxeoCompatibility.canUseES()) {
            request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice, vevent");
        } else {
            request = nuxeoSession.newRequest("Document.Query");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice, vevent");
        }
        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Pins/" + this.workspacePath;
    }
}
