package org.osivia.services.versions.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Versions plugins.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class VersionsPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-acl-management.plugin";


    /**
     * Constructor.
     */
    public VersionsPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = this.getMenubarModules(context);

        // Version menubar module
        MenubarModule module = new VersionsMenubarModule();
        modules.add(module);
    }

}
