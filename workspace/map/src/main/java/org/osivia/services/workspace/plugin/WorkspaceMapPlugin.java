package org.osivia.services.workspace.plugin;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Workspace map plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class WorkspaceMapPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-map.plugin";


    /** Taskbar service. */
    private final ITaskbarService taskbarService;


    /**
     * Constructor.
     */
    public WorkspaceMapPlugin() {
        super();

        // Taskbar service
        this.taskbarService = Locator.findMBean(ITaskbarService.class, ITaskbarService.MBEAN_NAME);
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
        // Taskbar items
        this.customizeTaskbarItems(context);
    }


    /**
     * Customize taskbar items.
     * 
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Taskbar factory
        TaskbarFactory factory = this.taskbarService.getFactory();
        
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);

        // Workspace map taskbar item
        PanelPlayer player = new PanelPlayer();
        player.setInstance("osivia-services-workspace-map-instance");
        TaskbarItem workspaceMap = factory.createStapledTaskbarItem("WORKSPACE_MAP", "WORKSPACE_MAP_TASK", "glyphicons glyphicons-map", player);
        items.add(workspaceMap);
    }

}
