package org.osivia.services.workspace.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get invitation Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetInvitationCommand implements INuxeoCommand {

    /** Invitation path. */
    private final String path;


    /**
     * Constructor.
     * @param path invitation path
     */
    public GetInvitationCommand(String path) {
        super();
        this.path = path;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND ecm:path = '").append(this.path).append("' ");

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        // Operation request results
        Documents results = (Documents) request.execute();

        // Result
        Document result;
        if ((results == null) || results.isEmpty() || (results.size() > 1)) {
            throw new NuxeoException(NuxeoException.ERROR_NOTFOUND);
        } else {
            result = results.get(0);
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("|");
        builder.append(this.path);
        return builder.toString();
    }

}
