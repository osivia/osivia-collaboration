package org.osivia.services.statistics.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoCompatibility;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * List documents command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class ListDocumentsCommand implements INuxeoCommand {

    /** Request. */
    private final String request;
    /** Nuxeo query filter context. */
    private final NuxeoQueryFilterContext filter;


    /**
     * Constructor.
     *
     * @param request request
     * @param filter Nuxeo query filter context
     */
    public ListDocumentsCommand(String request, NuxeoQueryFilterContext filter) {
        super();
        this.request = request;
        this.filter = filter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document WHERE ");
        query.append(NuxeoQueryFilter.addPublicationFilter(this.filter, this.request));
        query.append(" ORDER BY dc:created ASC");

        // Request
        OperationRequest request;
        if (NuxeoCompatibility.canUseES()) {
            request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        } else {
            request = nuxeoSession.newRequest("Document.Query");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore");
        }
        request.set("query", query.toString());

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.request;
    }

}
