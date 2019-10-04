/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import java.text.ParseException;

import javax.portlet.PortletContext;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Loïc Billon
 *
 */
@Service
public class QuotaReportingServiceImpl implements QuotaReportingService {

    /**
     * Batch service
     */
    @Autowired
    private IBatchService batchService;
    

	private QuotaComputer qc;
	
	/* (non-Javadoc)
	 * @see org.osivia.services.workspace.quota.reporting.portlet.controller.QuotaReportingService#installBatch(javax.portlet.PortletContext)
	 */
	@Override
    public void installBatch(PortletContext portletContext) throws PortalException {
    	qc = new QuotaComputer();
    	qc.setPortletContext(portletContext);
    	
    	try {
			batchService.addBatch(qc);
		} catch (ParseException e) {
			throw new PortalException(e);
		}
    	
    }

	/* (non-Javadoc)
	 * @see org.osivia.services.workspace.quota.reporting.portlet.controller.QuotaReportingService#uninstallBatch()
	 */
	@Override
    public void uninstallBatch() {
    	batchService.removeBatch(qc);
    }

}
