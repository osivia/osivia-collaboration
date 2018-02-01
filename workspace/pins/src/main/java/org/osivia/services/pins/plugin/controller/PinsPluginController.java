package org.osivia.services.pins.plugin.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.pins.plugin.fragment.PinsFragmentModule;
import org.osivia.services.pins.plugin.menubar.PinsMenuBarModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;

/**
 * Pins plugin controller
 * @author jbarberet
 *
 */
@Controller
public class PinsPluginController extends AbstractPluginPortlet {

	/** Plugin name. */
	private static final String PLUGIN_NAME = "pins.plugin";

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private PortletConfig portletConfig;
	@Autowired
	private IBundleFactory bundleFactory;
	
	/**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
        super.init();
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
	protected void customizeCMSProperties(CustomizationContext context) {
		// Menubar modules
		this.customizeMenubarModules(context);
		
		//Fragment type
		this.customizeFragmentTypes(context);
	}
	

	/**
	 * Customize menubar modules.
	 *
	 * @param context
	 *            customization context
	 */
	private void customizeMenubarModules(CustomizationContext context) {
		// Menubar modules
		List<MenubarModule> modules = this.getMenubarModules(context);

		// Forum menubar module
		MenubarModule pinsMenu = this.applicationContext.getBean(PinsMenuBarModule.class);
		modules.add(pinsMenu);
	}
	
	/**
	 * Customize fragment types
	 * @param context
	 */
	private void customizeFragmentTypes(CustomizationContext context)
	{
		List<FragmentType> types = this.getFragmentTypes(context);
		Bundle bundle = bundleFactory.getBundle(context.getLocale());
		
		FragmentType fragmentType = new FragmentType("pins", bundle.getString("FRAGMENT_TYPE_PINS"), this.applicationContext.getBean(PinsFragmentModule.class, this.getPortletContext()));
		types.add(fragmentType);
	}
	
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
