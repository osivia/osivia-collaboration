package org.osivia.services.rss.common.command;

import java.util.List;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.feedRss.portlet.repository.ItemRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Items Delete Nuxeo command.
 * Delete Items with the syncId 
 *
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemsDeleteCommand implements INuxeoCommand {

    private final List<String> items;

    /**
     * Constructor.
     *
     * @param syncId 
     */
    public ItemsDeleteCommand(List<String> items) {
        super();
        this.items = items;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {

		// Clause
		StringBuilder clause = new StringBuilder();
		clause.append("ecm:primaryType = 'RssItem' ");

		String filteredRequest = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());
		
		// Request
		OperationRequest request;
		request = nuxeoSession.newRequest("Document.Query");
		request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
		request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

		return request.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
