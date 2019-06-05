package org.osivia.services.widgets.issued.plugin.service;

import java.util.List;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

/**
 * Issued date plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface IssuedPluginService {

    /** Plugin name. */
    String PLUGIN_NAME = "issued.plugin";

    /** Portlet instance. */
    String INSTANCE = "osivia-services-widgets-issued-instance";


    /**
     * Customize menubar modules.
     * 
     * @param customizationContext customization context
     * @param menubarModules menubar modules
     */
    void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules);


    /**
     * Add menubar item.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext document context
     * @throws PortalException
     */
    void addMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException;

}
