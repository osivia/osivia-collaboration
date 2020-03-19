package org.osivia.services.rss.batch;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.osivia.portal.api.directory.v2.IDirProvider;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Frédéric Boudan
 */
public class RssProviderPortlet extends CMSPortlet{
	

	/** The provider (socle) */
	private IDirProvider provider;

	/** Batch rss */
	private SynchronizationRssBatch batch = new SynchronizationRssBatch();	
	
    protected static final Log logger = LogFactory.getLog(SynchronizationRssBatch.class);
    
    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        
        logger.info("Démarre la planification du batch de synchronisation");
        
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        
        IBatchService batchService = Locator.findMBean(IBatchService.class, IBatchService.MBEAN_NAME);

		try {
			batch.setPortletContext(getPortletContext());
			
			try {
				batchService.addBatch(batch);
			} catch (java.text.ParseException e) {
		        logger.error("Erreur lors du parsing de la synchronisation" + e.getMessage());
			}
		} catch (PortalException e) {
			logger.error("Erreur lors du parsing de la synchronisation" + e.getMessage());
			throw new PortletException(e);
		}
        
    }

    // @Override
    public void destroy() {

        super.destroy();
        logger.info("Arrêt la planification du batch de synchronisation");
        IBatchService batchService = Locator.findMBean(IBatchService.class, IBatchService.MBEAN_NAME);
        batchService.removeBatch(batch);
        
    }	

}
