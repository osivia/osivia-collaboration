package org.osivia.services.editor.common.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.editor.common.model.SearchScope;

/**
 * Search source document Nuxeo command abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class SearchSourceDocumentCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    protected static final String OPERATION_ID = "Document.QueryES";


    /**
     * Base path.
     */
    private String basePath;
    /**
     * Search filter.
     */
    private String filter;
    /**
     * Search scope.
     */
    private SearchScope scope;


    /**
     * Constructor.
     */
    public SearchSourceDocumentCommand() {
        super();
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        String clause = this.getClause();

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, clause);

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.set(Constants.HEADER_NX_SCHEMAS, "*");
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredClause);
        operationRequest.set("pageSize", 20);
        operationRequest.set("currentPageIndex", 0);

        return operationRequest.execute();
    }


    /**
     * Get NXQL clause.
     *
     * @return clause
     */
    protected abstract String getClause();


    @Override
    public String getId() {
        return null;
    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public SearchScope getScope() {
        return scope;
    }

    public void setScope(SearchScope scope) {
        this.scope = scope;
    }
}
