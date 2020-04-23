package org.osivia.services.rss.common.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * List Nuxeo command.
 *
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemListCommand implements INuxeoCommand {

    private final String syncId;

    public ItemListCommand(String syncId) {
		super();
        this.syncId = syncId;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		// Clause
		StringBuilder clause = new StringBuilder();
		clause.append("ecm:primaryType = 'RssItem' ");
		clause.append("AND rssi:syncId = '").append(this.syncId).append("' ");;

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
