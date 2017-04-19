package org.osivia.services.statistics.plugin;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Statistics plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class StatisticsPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "statistics.plugin";


    /**
     * Constructor.
     */
    public StatisticsPlugin() {
        super();
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
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Statistics
        TaskbarItem statistics = factory.createStapledTaskbarItem("STATISTICS", "STATISTICS_TASK", "glyphicons glyphicons-charts",
                "/default/templates/statistics");
        items.add(statistics);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }

}
