package org.osivia.services.workspace.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get trashed documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetTrashedDocumentsCommand implements INuxeoCommand {

    /** Base path. */
    private final String basePath;


    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public GetTrashedDocumentsCommand(String basePath) {
        super();
        this.basePath = basePath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document ");
        query.append("WHERE ecm:path STARTSWITH '").append(this.basePath).append("' ");
        query.append("AND ecm:mixinType <> 'HiddenInNavigation' ");
        query.append("AND ecm:isProxy = 0 ");
        query.append("AND ecm:currentLifeCycleState = 'deleted' ");
        query.append("AND ecm:isCheckedInVersion = 0 ");
        query.append("ORDER BY ecm:path ASC");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, common");
        request.set("query", query.toString());

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.basePath);
        return builder.toString();
    }

}
