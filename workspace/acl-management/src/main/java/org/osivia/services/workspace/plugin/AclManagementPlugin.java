package org.osivia.services.workspace.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Workspace ACL management plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class AclManagementPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-acl-management.plugin";


    /** Menubar module. */
    private final AclManagementMenubarModule menubarModule;


    /**
     * Constructor.
     */
    public AclManagementPlugin() {
        super();

        this.menubarModule = new AclManagementMenubarModule();
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
        List<MenubarModule> menubarModules = this.getMenubarModules(context);
        menubarModules.add(this.menubarModule);
    }

}
