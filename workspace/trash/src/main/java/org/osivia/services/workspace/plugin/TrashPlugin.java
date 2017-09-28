package org.osivia.services.workspace.plugin;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemRestriction;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.services.workspace.portlet.service.TrashService;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Trash plugin.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class TrashPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "trash.plugin";


    /**
     * Constructor.
     */
    public TrashPlugin() {
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
        // Taskbar items
        this.customizeTaskbarItems(context);
    }


    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Application context
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        // Portlet service
        TrashService service = applicationContext.getBean(TrashService.class);

        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();

        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Trash player
        PanelPlayer player;
        try {
            player = service.getPlayer(portalControllerContext);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }

        // Trash taskbar item
        TaskbarItem item = factory.createStapledTaskbarItem(TrashService.TASK_ID, "TRASH_TASK", "glyphicons glyphicons-bin", player);
        factory.restrict(item, TaskbarItemRestriction.EDITION);
        factory.preset(item, true, 10);

        items.add(item);
    }

}
