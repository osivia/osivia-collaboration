package org.osivia.services.workspace.sharing.plugin.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.workspace.sharing.plugin.service.SharingPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Sharing plugin controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Controller
public class SharingPluginController extends AbstractPluginPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Plugin service. */
    @Autowired
    private SharingPluginService service;


    /**
     * Constructor.
     */
    public SharingPluginController() {
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
    protected String getPluginName() {
        return SharingPluginService.PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext customizationContext) {
        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(customizationContext);
        this.service.customizeMenubarModules(customizationContext, menubarModules);
    }

}
