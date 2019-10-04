/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import java.util.Date;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
public class ReportQuotasCommand implements INuxeoCommand {

	public final static int PAGE_SIZE = 1000;
	
	private int currentPageIndex;
	
	public ReportQuotasCommand(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM DOcument WHERE ecm:primaryType = 'Workspace' AND ecm:path STARTSWITH '/default-domain/workspaces'");
        request.set("pageSize", PAGE_SIZE);
        request.set("currentPageIndex", currentPageIndex);
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, quota_computation, quota");
        
		return nuxeoSession.execute(request);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() +
				" page:"+currentPageIndex + 
				" " + new Date().getTime();
	}

}
