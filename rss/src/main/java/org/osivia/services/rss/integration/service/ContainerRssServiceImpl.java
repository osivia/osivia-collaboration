package org.osivia.services.rss.integration.service;

import javax.portlet.PortletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.Model.ContainerRssModel;
import org.osivia.services.rss.integration.repository.ContainerRssRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RSS service interface
 * Lecture des flux RSS
 * 
 * @author Frédéric Boudan
 *
 */
public class ContainerRssServiceImpl implements ContainerRssService {

	/** logger */
	protected static final Log logger = LogFactory.getLog(ContainerRssServiceImpl.class);	
	
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";
    static final String CATEGORY = "category";
    static final String SOURCE = "source";
    static final String ENCLOSURE = "enclosure";
    
    /**
     * Calendar repository.
     */
    @Autowired
    protected ContainerRssRepository repository;    
    
    
    /**
     * {@inheritDoc}
     */
    public void getListContainer(PortalControllerContext portalControllerContext, ContainerRssModel containerRss) throws PortletException {

        this.repository.getListContainerRss(portalControllerContext, containerRss);
    }

}
