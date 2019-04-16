package org.osivia.services.workspace.sharing.plugin.service;

import java.util.List;
import java.util.Map;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;

/**
 * Sharing plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingPluginService {

    /** Plugin name. */
    String PLUGIN_NAME = "sharing.plugin";

    /** Sharing portlet instance. */
    String SHARING_INSTANCE = "osivia-services-workspace-sharing-instance";


    /**
     * Customize menubar modules.
     * 
     * @param customizationContext customization context
     * @param menubarModules menubar modules
     */
    void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules);


    /**
     * Customize list templates.
     * 
     * @param customizationContext customization context
     * @param listTemplates list templates
     */
    void customizeListTemplates(CustomizationContext customizationContext, Map<String, ListTemplate> listTemplates);


    /**
     * Add sharing menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext document context
     * @throws PortalException
     */
    void addMenubarItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException;


    /**
     * Clear menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext document context
     * @throws PortalException
     */
    void clearMenubarItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException;


}
