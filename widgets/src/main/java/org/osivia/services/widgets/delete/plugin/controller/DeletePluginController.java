package org.osivia.services.widgets.delete.plugin.controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.delete.plugin.service.DeletePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.util.List;

/**
 * Delete plugin controller.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Controller
public class DeletePluginController extends AbstractPluginPortlet {

    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;

    /**
     * Plugin service.
     */
    @Autowired
    private DeletePluginService service;


    /**
     * Constructor.
     */
    public DeletePluginController() {
        super();
    }


    /**
     * Post-construct.
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


    @Override
    protected String getPluginName() {
        return DeletePluginService.PLUGIN_NAME;
    }


    @Override
    protected void customizeCMSProperties(CustomizationContext customizationContext) {
        // Customize menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(customizationContext);
        this.service.customizeMenubarModules(customizationContext, menubarModules);
    }

}
