package org.osivia.services.workspace.portlet.repository;

import java.text.Normalizer;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchPersonsCommand implements INuxeoCommand  {
	
	
	private String filter;
	
	public SearchPersonsCommand(String filter) {
		
		// Remove accents in search query
		filter = Normalizer.normalize(filter, Normalizer.Form.NFD);
		filter = filter.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		
		this.filter = filter;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'UserProfile' ");
        
        clause.append("AND (ttc_userprofile:login ILIKE '%").append(this.filter);
        clause.append("%' OR dc:title ILIKE '%").append(this.filter);
        clause.append("%' OR ttc_userprofile:mail ILIKE '%").append(this.filter);
        clause.append("%' OR ttc_userprofile:mailaca ILIKE '%").append(this.filter);
        clause.append("%') ");

        clause.append(" AND ttc_userprofile:shownInSearch = true ");

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        // Operation request results
        Documents results = (Documents) request.execute();

        return results;
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName().concat("/").concat(filter);
	}

	
	
}
