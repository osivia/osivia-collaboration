package org.osivia.services.workspace.edition.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Workspace edition plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class WorkspaceEditionPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-edition.plugin";


    /** Menubar module. */
    private final WorkspaceEditionMenubarModule menubarModule;


    /**
     * Constructor.
     */
    public WorkspaceEditionPlugin() {
        super();

        this.menubarModule = new WorkspaceEditionMenubarModule();
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
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(context);
        menubarModules.add(this.menubarModule);
    }

}
