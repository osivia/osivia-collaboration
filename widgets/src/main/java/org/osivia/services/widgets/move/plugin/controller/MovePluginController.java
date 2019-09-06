package org.osivia.services.widgets.move.plugin.controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.move.plugin.service.MovePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.util.List;

/**
 * Move plugin controller.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Controller
public class MovePluginController extends AbstractPluginPortlet {

    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;

    /**
     * Plugin service.
     */
    @Autowired
    private MovePluginService service;


    /**
     * Constructor.
     */
    public MovePluginController() {
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
        return MovePluginService.PLUGIN_NAME;
    }


    @Override
    protected void customizeCMSProperties(CustomizationContext customizationContext) {
        // Customize menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(customizationContext);
        this.service.customizeMenubarModules(customizationContext, menubarModules);
    }


    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}
