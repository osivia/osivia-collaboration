package org.osivia.services.rss.common.service;

import java.util.List;
import java.util.Set;

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
 * 
 * @author Frédéric Boudan
 *
 */
@Service
public class ContainerRssServiceImpl implements ContainerRssService {

    /** Repository. */
    @Autowired
    public ContainerRepository repository;
	
	/** logger */
	protected static final Log logger = LogFactory.getLog(ContainerRssServiceImpl.class);
	
    /**
     * {@inheritDoc}
     */
    public List<ContainerRssModel> getListContainer(PortalControllerContext portalControllerContext) throws PortletException {

    	List<ContainerRssModel> containers = this.repository.getListContainerRss(portalControllerContext);    	
        return containers; 
        
    }
	
    public Set<String> getMapContainer(PortalControllerContext portalControllerContext) throws PortletException {

    	Set<String> map = this.repository.getMapContainer(portalControllerContext);    	
        return map; 
    }

    public void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException {

    	this.repository.creatContainer(portalControllerContext, model);  	
    }

    /**
     * Remove container service
     */
	public void removeContainer(PortalControllerContext portalControllerContext, String docid)
			throws PortletException {
    	this.repository.remove(portalControllerContext, docid);
		
	}    
    
}
