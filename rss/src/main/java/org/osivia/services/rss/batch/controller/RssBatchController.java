package org.osivia.services.rss.batch.controller;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.osivia.services.rss.batch.model.RssBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletContext;
import java.text.ParseException;

/**
 * RSS batch controller.
 */
@Controller
public class RssBatchController {

    /**
     * Batch service.
     */
    @Autowired
    private IBatchService batchService;

    /**
     * Batch.
     */
    @Autowired
    private RssBatch batch;


    /**
     * Constructor.
     */
    public RssBatchController() {
        super();
    }


    /**
     * Post-construct.
     */
    @PostConstruct
    public void postConstruct() {
        try {
            this.batchService.addBatch(this.batch);
        } catch (ParseException | PortalException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        this.batchService.removeBatch(this.batch);
    }

}
