package org.osivia.services.sets.edition.portlet.repository.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Search documents Nuxeo command
 *
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class SearchDocumentsCommand implements INuxeoCommand {


    /** Operation request identifier. */
    private static final String OPERATION_ID = "Document.QueryES";
	/** Select2 result page size */
	private static final String SELECT2_RESULTS_PAGE_SIZE = "10";
    /** Request schemas. */
    private static final String SCHEMAS = "dublincore, common, toutatice, file";

    /** Context path. */
    private final String contextPath;
    /** Search criteria */
    private final String filter;
    /** Page number */
    private final int page;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public SearchDocumentsCommand(String contextPath, String filter, int page) {
        super();
        this.contextPath = contextPath;
        this.filter = filter;
        this.page = page;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append(" ecm:path STARTSWITH '").append(contextPath).append("' ");
        clause.append("AND dc:title IS NOT NULL ");
        clause.append("AND ttc:webid IS NOT NULL ");
        if (StringUtils.isNotBlank(this.filter)) {
            clause.append("AND (ecm:fulltext = '").append(this.filter).append("' or ecm:fulltext = '").append(this.filter).append("*') ");
        }
        clause.append(" ORDER BY dc:title ASC");

        // NXQL filtered request
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.set("pageSize", SELECT2_RESULTS_PAGE_SIZE);
        operationRequest.set("currentPageIndex", this.page);
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);
        operationRequest.set(Constants.HEADER_NX_SCHEMAS, SCHEMAS);

        return operationRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Sets/" + this.contextPath;
    }

}
