package org.osivia.services.workspace.quota.common;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * 
 * Get procedure instance by procedure uuid
 * @author Loïc Billon
 *
 */
public class GetProcedureInstanceCommand implements INuxeoCommand {

	private final static Log logger = LogFactory.getLog("batch");
	
	private String uuid;

	public GetProcedureInstanceCommand(String uuid) {
		this.uuid = uuid;
		
	}
	
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:globalVariablesValues.uuid = '").append(this.uuid).append("' ");

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        // Operation request results
        Documents results = (Documents) request.execute();

        // Result
        Document result = null;
        if ((results == null) || results.isEmpty() ) {
       	  	logger.error("ProcedureInstance for "+uuid+" not found");

        } else if (results.size() > 1) {
        	logger.error("Multiple ProcedureInstance found for "+uuid);
        	
        } else {
            result = results.get(0);
        }
        
        return result;
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName() + "/" + uuid + "/" + new Date().getTime();
	}

}
