package org.osivia.services.search.selector.type.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.selector.type.portlet.model.TypeSelectorForm;
import org.osivia.services.search.selector.type.portlet.model.TypeSelectorSettings;

/**
 * Type selector portlet service interface.
 * 
 * @author Loïc Billon
 */
public interface TypeSelectorService {

    /** Search type selector identifier. */
    String TYPE_SELECTOR_ID = "type";
	

    /** Label window property. */
    static final String LABEL_WINDOW_PROPERTY = "foad.type-selector.label";
	
    /**
     * Get portlet settings.
     * 
     * @param portalControllerContext portal controller context
     * @return portlet settings
     * @throws PortletException
     */
    TypeSelectorSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save portlet settings.
     * 
     * @param portalControllerContext portal controller context
     * @param settings portlet settings
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, TypeSelectorSettings settings) throws PortletException;


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    TypeSelectorForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Select.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void select(PortalControllerContext portalControllerContext, TypeSelectorForm form) throws PortletException;

}
