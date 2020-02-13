package org.osivia.services.search.selector.scope.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorForm;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorSettings;

/**
 * Scope selector portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface ScopeSelectorService {

    /**
     * Get portlet settings.
     * 
     * @param portalControllerContext portal controller context
     * @return portlet settings
     * @throws PortletException
     */
    ScopeSelectorSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save portlet settings.
     * 
     * @param portalControllerContext portal controller context
     * @param settings portlet settings
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, ScopeSelectorSettings settings) throws PortletException;


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    ScopeSelectorForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Select.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void select(PortalControllerContext portalControllerContext, ScopeSelectorForm form) throws PortletException;

}
