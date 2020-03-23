package org.osivia.services.rss.batch;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.service.ContainerRssService;
import org.osivia.services.rss.common.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Batch de synchronisation des flux RSS
 *
 * @author Frédéric Boudan
 */
@Component
public class SynchronizationRssBatch extends AbstractBatch {

    protected static final Log logger = LogFactory.getLog(SynchronizationRssBatch.class);
    private static final String CRON_DEFAULT_VALUE = "0 0/2 * * * ?";
    
    /** Feed RSS service. */
    @Autowired
    protected FeedService service;
    
    /** Container RSS service. */
    @Autowired
    protected ContainerRssService serviceContainer;
    
    /**
     * Portlet context.
     */
    private static PortletContext portletContext;
    
    public SynchronizationRssBatch() {
    }

    @Override
    public boolean isRunningOnMasterOnly() {
        return true;
    }

    @Override
	public String getJobScheduling() {
		String quotaCron = System.getProperty("osivia.services.quota.cron");
		if (StringUtils.isNotBlank(quotaCron)) {
			return quotaCron;
		} else
			return System.getProperty("CRON_BATCH_RSS", CRON_DEFAULT_VALUE);
	}
    

    @Override
    public void execute(Map<String, Object> parameters) {
        logger.info("Exécution du batch RSS");
        
        // Récupération des containers 
        List<ContainerRssModel> containers = null;
        try {
        	containers = serviceContainer.getListContainer(new PortalControllerContext(portletContext, null, null));
        } catch (Exception e) {
        	logger.error("Synchornisation: Problème lors récupération des containers :" + e.getMessage());
		}
        
        if(containers != null) {
        	
	        for(ContainerRssModel container: containers) {
	            // Pour chaque conteneur, récupération des flux associés
	        	try {
	        		List<FeedRssModel> feeds = service.fillFeed(container.getDoc());
	        		container.setFeedSources(feeds);
				} catch (PortletException e1) {
					logger.error("Synchronisation: Problème lors récupération des flux du conteneur :" + container.getName());
				}
	        	
	        	// Synchronisation des flux par conteneur
				try {
					service.synchro(new PortalControllerContext(portletContext, null, null), container);
				} catch (PortletException e) {
					logger.error("Synchronisation: Problème lors de la mise à jour des flux :" + e.getMessage());
				}
	        }
        }

        logger.info("Fin de la synchronisation");

    }

    public void setPortletContext(PortletContext portletContext) {
    	SynchronizationRssBatch.portletContext = portletContext;
    }

}
