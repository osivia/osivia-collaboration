package org.osivia.services.workspace.quota.plugin;

import java.util.List;

import javax.portlet.PortletContext;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.workspace.quota.plugin.menubar.QuotaMenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Quota plugin.
 *
 * @author Loïc Billon
 * @see AbstractPluginPortlet
 */
public class QuotaPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "quota.plugin";


    /** Menubar module. */
    private final MenubarModule menubarModule;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public QuotaPlugin() {
        super();
        this.menubarModule = new QuotaMenubarModule();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
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
    protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = getPortletContext();

        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(context);
        menubarModules.add(this.menubarModule);

    }

}
