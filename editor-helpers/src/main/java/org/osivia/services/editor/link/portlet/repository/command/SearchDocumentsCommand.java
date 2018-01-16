package org.osivia.services.editor.link.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.editor.link.portlet.repository.EditorLinkRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search documents Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchDocumentsCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String OPERATION_ID = "Document.QueryES";
    /** Request schemas. */
    private static final String SCHEMAS = "dublincore, toutatice, file";


    /** Search path. */
    private final String path;
    /** Search filter. */
    private final String filter;
    /** Search pagination page number. */
    private final int page;


    /**
     * Constructor.
     *
     * @param path   search path
     * @param filter search filter
     * @param page   search pagination page number
     */
    public SearchDocumentsCommand(String path, String filter, int page) {
        super();
        this.path = path;
        this.filter = filter;
        this.page = page;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // NXQL clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:path STARTSWITH '").append(this.path).append("' ");
        clause.append("AND dc:title IS NOT NULL ");
        clause.append("AND ttc:webid IS NOT NULL ");
        if (StringUtils.isNotBlank(this.filter)) {
            clause.append("AND (dc:title ILIKE '").append(this.filter).append("%' or ecm:fulltext = '").append(this.filter).append("' ) ");
        }
        clause.append("ORDER BY dc:title ASC");

        // NXQL filtered request
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, clause.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.set("pageSize", EditorLinkRepository.SELECT2_RESULTS_PAGE_SIZE);
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
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getCanonicalName());
        builder.append("|");
        builder.append(this.path);
        builder.append("|");
        builder.append(this.filter);
        builder.append("|");
        builder.append(this.page);
        return builder.toString();
    }

}
