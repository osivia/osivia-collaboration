/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
public class QuotaSearchWksCommand implements INuxeoCommand {

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ts = sdf.format(c.getTime());
		
		// Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, quota_computation, webcontainer");
        request.set("pageSize", "1000");
        request.set("currentPageIndex", "0");
        request.set("query", "SELECT * FROM Document WHERE ecm:path STARTSWITH '/default-domain/workspaces' AND ecm:primaryType = 'Workspace' "+
        		"AND (qtc:lastDateCheck < TIMESTAMP '"+ts+"' OR qtc:lastDateCheck IS NULL) "+
        		"AND ecm:isVersion = 0 AND ecm:currentLifecycleState <> 'deleted' " );
        
        return request.execute();
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() + " " +
		new Date().getTime();
	}

}
