package org.osivia.services.widgets.rename.plugin.service;

import java.util.List;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

/**
 * Rename plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface RenamePluginService {

    /** Plugin name. */
    String PLUGIN_NAME = "rename.plugin";

    /** Rename portlet instance. */
    String RENAME_INSTANCE = "osivia-services-widgets-rename-instance";


    /**
     * Customize menubar modules.
     * 
     * @param customizationContext customization context
     * @param menubarModules menubar modules
     */
    void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules);


    /**
     * Add rename menubar item.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext document context
     * @throws PortalException
     */
    void addMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException;

}
