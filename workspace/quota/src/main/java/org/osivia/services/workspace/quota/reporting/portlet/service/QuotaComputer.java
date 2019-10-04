/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.services.workspace.quota.portlet.repository.GetQuotaCommand;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import net.sf.json.JSONObject;

/**
 * Batch that computes quotas (min every day)
 * @author Loïc Billon
 *
 */
public class QuotaComputer extends AbstractBatch {

	private final static Log logger = LogFactory.getLog("batch");

    /** Portlet context. */
    private static PortletContext portletContext;
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.batch.AbstractBatch#getJobScheduling()
	 */
	@Override
	public String getJobScheduling() {
		return "0 0/3 * * * ?";
	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.batch.AbstractBatch#execute(java.util.Map)
	 */
	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {
		
		Date startD = new Date();
		logger.warn("Quota batch started");
		

		NuxeoController nuxeoController = new NuxeoController(portletContext);

		nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
		nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

		Documents workspaces = (Documents) nuxeoController.executeNuxeoCommand(new QuotaSearchWksCommand());
		
		for(Document workspace : workspaces) {
			Blob quotaInfos =  (Blob) nuxeoController.executeNuxeoCommand(new GetQuotaCommand(workspace.getPath()));        
			
	        if (quotaInfos != null) {
	            
	            String quotaInfosContent;
				try {
					quotaInfosContent = IOUtils.toString(quotaInfos.getStream(), "UTF-8");
				} catch (IOException e) {
					throw new PortalException( e);
				}

	            JSONObject quotaContent = JSONObject.fromObject(quotaInfosContent);
	            
	            long treeSize = quotaContent.getLong("treesize");
	            long quota = quotaContent.getLong("quota");
	            
	            logger.warn("Quota : "+workspace.getTitle()+" ("+treeSize+"/"+quota+")");
	            
	            nuxeoController.executeNuxeoCommand(new UpdateQuotaComputationCommand(workspace, treeSize));
	         }
		}
		
		Date endD = new Date();
		long duration = endD.getTime() - startD.getTime();
		
		logger.warn("Quota batch ended (duration : "+duration+"ms)");
	}

	public void setPortletContext(PortletContext portletContext) {
		QuotaComputer.portletContext = portletContext;
	}
}
