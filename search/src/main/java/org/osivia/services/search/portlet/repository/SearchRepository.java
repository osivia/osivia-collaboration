package org.osivia.services.search.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.common.repository.CommonRepository;
import org.osivia.services.search.portlet.model.TaskPath;

/**
 * Search portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface SearchRepository extends CommonRepository {

    /**
     * Get search task path.
     * 
     * @param portalControllerContext portal controller context
     * @param basePath base path
     * @return path
     * @throws PortletException
     */
    TaskPath getSearchTaskPath(PortalControllerContext portalControllerContext, String basePath) throws PortletException;

}
