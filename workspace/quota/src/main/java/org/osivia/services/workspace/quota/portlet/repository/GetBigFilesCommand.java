package org.osivia.services.workspace.quota.portlet.repository;

import java.util.Date;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * List files that are bigger than 10MB.
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetBigFilesCommand implements INuxeoCommand {

	private String path;
	
	private GetBigFilesCommand(String path) {
		this.path = path;
	}
	
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		// Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, common");
        request.set("pageSize", "25");
        request.set("currentPageIndex", "0");
        request.set("query", "SELECT * FROM Document WHERE ecm:path STARTSWITH '"+path+
        		"' AND ecm:isVersion = 0 AND common:size > 10000000 ORDER BY common:size DESC ");
        
        return request.execute();
	}

	@Override
	public String getId() {
		return this.getClass().getSimpleName() +
				"/"+path+"/"
				+new Date().getTime();
		
	}

}
