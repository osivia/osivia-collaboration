package org.osivia.services.rss.batch;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Frédéric Boudan
 */
public class RssProviderPortlet extends CMSPortlet{
	
	/** Batch rss */
	private SynchronizationRssBatch batch = new SynchronizationRssBatch();
	
    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        
        IBatchService batchService = Locator.findMBean(IBatchService.class, IBatchService.MBEAN_NAME);

		try {
			batch.setPortletContext(getPortletContext());
			
			try {
				batchService.addBatch(batch);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PortalException e) {
			throw new PortletException(e);
		}
        
    }

    // @Override
    public void destroy() {

        super.destroy();

        IBatchService batchService = Locator.findMBean(IBatchService.class, IBatchService.MBEAN_NAME);
        batchService.removeBatch(batch);
        
    }	

}
