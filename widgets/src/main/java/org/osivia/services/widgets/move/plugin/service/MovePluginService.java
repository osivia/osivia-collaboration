package org.osivia.services.widgets.move.plugin.service;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import java.util.List;

/**
 * Move plugin service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MovePluginService {

    /**
     * Plugin name.
     */
    String PLUGIN_NAME = "move.plugin";

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-widgets-move-instance";


    /**
     * Customize menubar modules.
     *
     * @param customizationContext customization context
     * @param menubarModules       menubar modules
     */
    void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules);


    /**
     * Update menubar.
     *
     * @param portalControllerContext portal controller context
     * @param menubar                 menubar
     * @param documentContext         document context
     */
    void updateMenubar(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException;

}
