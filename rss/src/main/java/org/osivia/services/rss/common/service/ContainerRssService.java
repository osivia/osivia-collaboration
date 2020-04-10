package org.osivia.services.rss.common.service;

import java.util.List;
import java.util.Set;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.model.ContainerRssModel;

/**
 * Container RSS service interface
 * 
 * @author Frédéric Boudan
 *
 */
public interface ContainerRssService {

    /**
     * get List Container.
     *
     * @param portalControllerContext portal controller context
     * @return List<ContainerRssModel>
     * @throws PortletException
     */
	List<ContainerRssModel> getListContainer(PortalControllerContext portalControllerContext) throws PortletException;
	
    /**
     * get Map Container.
     *
     * @param portalControllerContext portal controller context
     * @return Map
     * @throws PortletException
     */
	Set<String> getMapContainer(PortalControllerContext portalControllerContext) throws PortletException;	
	
    /**
     * create Container.
     *
     * @param portalControllerContext portal controller context
     * @param model ContainerRssModel
     * @throws PortletException
     */
	void creatContainer(PortalControllerContext portalControllerContext, ContainerRssModel model) throws PortletException;
	
    /**
     * remove Container.
     *
     * @param portalControllerContext portal controller context
     * @param docid String
     * @throws PortletException
     */
	void removeContainer(PortalControllerContext portalControllerContext, String docid) throws PortletException;	

}
