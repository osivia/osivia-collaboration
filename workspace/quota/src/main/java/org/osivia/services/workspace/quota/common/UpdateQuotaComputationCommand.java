/**
 * 
 */
package org.osivia.services.workspace.quota.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
public class UpdateQuotaComputationCommand implements INuxeoCommand {

	private Document workspace;
	private long treeSize;
	private String uuid;


	public UpdateQuotaComputationCommand(Document workspace, long treeSize, String uuid) {
		this.workspace = workspace;
		this.treeSize = treeSize;
		this.uuid = uuid;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ts = sdf.format(c.getTime());
        documentService.setProperty(this.workspace, "qtc:currentSize", Long.toString(treeSize));		
        documentService.setProperty(this.workspace, "qtc:lastDateCheck", ts);
        documentService.setProperty(this.workspace, "qtc:uuid", uuid);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() + " " +
		workspace.getPath() + " " +
		new Date().getTime();
	}

}
