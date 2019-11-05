package org.osivia.services.widgets.delete.plugin.service;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import java.util.List;

/**
 * Delete plugin service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DeletePluginService {

    /** Plugin name. */
    String PLUGIN_NAME = "delete.plugin";


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
