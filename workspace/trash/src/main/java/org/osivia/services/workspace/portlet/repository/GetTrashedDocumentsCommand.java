package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
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
        request.set("currentPageIndex", "0");
        request.set("pageSize", "1000");

        PaginableDocuments documents = (PaginableDocuments) request.execute();
        List<Document> all = documents.list();
        
        if (documents.getPageCount() > 1) {
        	for(int i = 1; i < documents.getPageCount(); i++) {
        		request.set("currentPageIndex", i);
        		
        		PaginableDocuments nextPage = (PaginableDocuments) request.execute();
        		all.addAll(nextPage.list());
        	}
        }
        
        return all;
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
