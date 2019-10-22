package org.osivia.services.rss.feedRss.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RSS service interface
 * Lecture des flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
@Service
public class FeedServiceImpl implements FeedService {

    /** Repository. */
    @Autowired
    public ContainerRepository repository;
	
	/** logger */
	protected static final Log logger = LogFactory.getLog(FeedServiceImpl.class);	
	
    /**
     * {@inheritDoc}
     */
    public List<ContainerRssModel> getListFeed(PortalControllerContext portalControllerContext) throws PortletException {

    	List<ContainerRssModel> containers = this.repository.getListContainerRss(portalControllerContext);    	
        return containers;    	
    }

    public void creatFeed(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {

    	this.repository.creatFeed(portalControllerContext, model);    	
    }
}
