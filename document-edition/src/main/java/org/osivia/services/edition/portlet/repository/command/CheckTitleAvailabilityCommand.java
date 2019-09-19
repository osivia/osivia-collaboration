package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Check title availability Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckTitleAvailabilityCommand implements INuxeoCommand {

    /**
     * Parent document identifier.
     */
    private final String parentId;
    /**
     * Document title.
     */
    private final String title;


    /**
     * Constructor.
     *
     * @param parentId parent document identifier
     * @param title    document title
     */
    public CheckTitleAvailabilityCommand(String parentId, String title) {
        super();
        this.parentId = parentId;
        this.title = title;
    }


    @Override
    public Boolean execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:parentId = '").append(this.parentId).append("' ");
        clause.append("AND dc:title ILIKE '").append(StringUtils.replace(this.title, "'", "\\'")).append("' ");

        // NXQL filtered request
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        // Results
        Documents results = (Documents) request.execute();

        return results.isEmpty();
    }


    @Override
    public String getId() {
        return null;
    }

}
