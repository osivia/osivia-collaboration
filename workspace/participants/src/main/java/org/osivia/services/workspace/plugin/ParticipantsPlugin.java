package org.osivia.services.workspace.plugin;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.services.workspace.portlet.service.ParticipantsService;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Statistics plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class ParticipantsPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "participants.plugin";


    /** Portlet service. */
    private ParticipantsService service;


    /**
     * Constructor.
     */
    public ParticipantsPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        // Taskbar items
        this.customizeTaskbarItems(context);
    }


    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Portlet service
        ParticipantsService service = this.getService();

        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();

        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Participants player
        PanelPlayer player;
        try {
            player = service.getPlayer(portalControllerContext);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }

        // Participants taskbar item
        TaskbarItem item = factory.createStapledTaskbarItem(ParticipantsService.TASK_ID, "WORKSPACE_PARTICIPANTS_TASK", "glyphicons glyphicons-group", player);
        items.add(item);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * Get workspace creation service.
     * 
     * @return service
     */
    private ParticipantsService getService() {
        if (this.service == null) {
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            this.service = applicationContext.getBean(ParticipantsService.class);
        }
        return this.service;
    }

}
