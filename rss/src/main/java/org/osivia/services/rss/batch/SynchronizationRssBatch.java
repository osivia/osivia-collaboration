package org.osivia.services.rss.batch;

import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.services.rss.feedRss.portlet.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Batch de synchronisation des flux RSS
 *
 * @author Frédéric Boudan
 */
public class SynchronizationRssBatch extends AbstractBatch {

    protected static final Log logger = LogFactory.getLog(SynchronizationRssBatch.class);
    private static final String CRON_DEFAULT_VALUE = "0 0/2 * * * ?";
    
    /** Feed RSS service. */
    @Autowired
    protected FeedService service;
    
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
        return System.getProperty("CRON_BATCH_RSS", CRON_DEFAULT_VALUE);
    }

    @Override
    public void execute(Map<String, Object> parameters) {
        logger.info("Exécution du batch RSS");

        NuxeoController nuxeoController = new NuxeoController(portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

//        service.synchro(portalControllerContext);
//        
//        INuxeoCommand nuxeoCommand = new ListInteractikAgendaCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED);
//        Documents agendasInteractik = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
//
//        // 2) For each interactik agenda, search synchronization source
//        for (Document agenda : agendasInteractik) {
//            PropertyList propertyList = (PropertyList) agenda.getProperties().get(LIST_SOURCE_SYNCHRO);
//            if (propertyList != null) {
//                CalendarSynchronizationSource source;
//                for (int i = 0; i < propertyList.size(); i++) {
//                    PropertyMap map = propertyList.getMap(i);
//                    source = new CalendarSynchronizationSource();
//                    source.setUrl(map.getString(URL_SYNCHRONIZATION));
//                    source.setId(map.getString(SOURCEID_SYNCHRONIZATION));
//                    // 3) Synchronize each synchronization source
//                    try {
//                        Map<EventKey, EventToSync> mapEvents = getEventsFromUrl(new URL(source.getUrl()), source.getId());
//                        nuxeoCommand = new InteractikSynchronizationCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, agenda.getPath(), mapEvents);
//                        nuxeoController.executeNuxeoCommand(nuxeoCommand);
//                    } catch (MalformedURLException e) {
//                        logger.error("Impossible d'accéder à l'URL de synchronisation, détail:" + e.getMessage());
//                    }
//                }
//            }
//        }

    }

    public void setPortletContext(PortletContext portletContext) {
    	SynchronizationRssBatch.portletContext = portletContext;
    }


}
