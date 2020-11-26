package org.osivia.services.editor.image.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search image documents Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchImageDocumentsCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    private static final String OPERATION_ID = "Document.QueryES";


    /**
     * Search filter.
     */
    private final String filter;


    /**
     * Constructor.
     *
     * @param filter search filter
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SearchImageDocumentsCommand(String filter) {
        super();
        this.filter = filter;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("(ecm:primaryType = 'Picture' OR (ecm:primaryType = 'File' AND file:content/mime-type LIKE 'image%')) ");
        if (StringUtils.isNotBlank(this.filter)) {
            clause.append("AND ecm:fulltext = '" + this.filter + "' ");
        }

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, clause.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.set(Constants.HEADER_NX_SCHEMAS, "*");
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredClause);
        operationRequest.set("pageSize", 50);
        operationRequest.set("currentPageIndex", 0);

        return operationRequest.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
