package org.osivia.services.workspace.filebrowser.plugin.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.services.workspace.filebrowser.plugin.service.FileBrowserPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * File browser plugin controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
public class FileBrowserPluginController extends AbstractPluginPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Plugin service. */
    @Autowired
    private FileBrowserPluginService service;


    /**
     * Constructor.
     */
    public FileBrowserPluginController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPluginName() {
        return FileBrowserPluginService.PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return DEFAULT_DEPLOYMENT_ORDER + 1;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeCMSProperties(CustomizationContext customizationContext) {
        // Players
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(customizationContext);
        this.service.customizePlayerModules(customizationContext, players);
        
        /// Menubar modules
        List<MenubarModule> modules = getMenubarModules(customizationContext);

        MenubarModule mbModule = new ZipCreationMenubarModule();
        modules.add(mbModule);
    }

}
