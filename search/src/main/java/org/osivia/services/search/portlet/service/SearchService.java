package org.osivia.services.search.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.portlet.model.SearchForm;

/**
 * Search portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SearchService {

    /** Search scope selector identifier. */
    String SCOPE_SELECTOR_ID = "scope";


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    SearchForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Search.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @return redirection URL
     * @throws PortletException
     */
    String search(PortalControllerContext portalControllerContext, SearchForm form) throws PortletException;

}
