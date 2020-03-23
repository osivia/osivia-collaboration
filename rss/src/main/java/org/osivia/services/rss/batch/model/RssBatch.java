package org.osivia.services.rss.batch.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.AbstractBatch;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.service.ContainerRssService;
import org.osivia.services.rss.common.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import java.util.List;
import java.util.Map;

/**
 * RSS batch.
 *
 * @see AbstractBatch
 */
@Component
public class RssBatch extends AbstractBatch {

    /**
     * Cron default value.
     */
    private static final String CRON_DEFAULT_VALUE = "0 0/2 * * * ?";


    /**
     * Log.
     */
    private final Log log;

    /**
     * Job scheduling.
     */
    private final String scheduling;


    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;

    /**
     * Feed RSS service.
     */
    @Autowired
    private FeedService feedService;

    /**
     * Container RSS service.
     */
    @Autowired
    private ContainerRssService containerRssService;


    /**
     * Constructor.
     */
    public RssBatch() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());

        // Job scheduling
        this.scheduling = System.getProperty("CRON_BATCH_RSS", CRON_DEFAULT_VALUE);
    }


    @Override
    public String getJobScheduling() {
        return this.scheduling;
    }


    @Override
    public void execute(Map<String, Object> parameters) throws PortalException {
        this.log.info("Exécution du batch RSS");

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, null, null);

        // Récupération des containers
        List<ContainerRssModel> containers = null;
        try {
            containers = this.containerRssService.getListContainer(portalControllerContext);
        } catch (Exception e) {
            this.log.error("Synchornisation: Problème lors récupération des containers :" + e.getMessage());
        }

        if (containers != null) {
            for (ContainerRssModel container : containers) {
                // Pour chaque conteneur, récupération des flux associés
                try {
                    List<FeedRssModel> feeds = this.feedService.fillFeed(container.getDoc());
                    container.setFeedSources(feeds);
                } catch (PortletException e) {
                    this.log.error("Synchronisation: Problème lors récupération des flux du conteneur :" + container.getName());
                }

                // Synchronisation des flux par conteneur
                try {
                    this.feedService.synchro(portalControllerContext, container);
                } catch (PortletException e) {
                    this.log.error("Synchronisation: Problème lors de la mise à jour des flux :" + e.getMessage());
                }
            }
        }

        this.log.info("Fin de la synchronisation");
    }

}
