package org.osivia.services.workspace.edition.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;

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


    /**
     * Constructor.
     */
    public WorkspaceEditionPlugin() {
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
        this.customizeMenubarModules(context);

        // Taskbar items
        this.customizeTaskbarItems(context);
    }


    private void customizeMenubarModules(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = this.getMenubarModules(context);

        // Workspace edition
        MenubarModule workspaceEdition = new WorkspaceEditionMenubarModule();
        modules.add(workspaceEdition);
    }


    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Workspace editorial
        TaskbarItem editorial = factory.createHiddenCmsTaskbarItem(WorkspaceEditionService.WORKSPACE_EDITORIAL_TASK_ID,
                "WORKSPACE_EDITION_WORKSPACE_EDITORIAL_TASK", "Note");
        items.add(editorial);
    }

}
