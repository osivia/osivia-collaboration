package org.osivia.services.sets.quickaccess.plugin.portlet;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.set.SetType;
import org.osivia.services.sets.quickaccess.plugin.menubar.QuickAccessMenuBarModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Pins plugin controller
 * @author Julien Barberet
 *
 */
@Controller
public class QuickAccessPluginController extends AbstractPluginPortlet {

	private static final String QUICKACCESS_ID = "quickAccess";
	/** Plugin name. */
	private static final String PLUGIN_NAME = "quickAccess.plugin";

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private PortletConfig portletConfig;
	
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
		
		//Set type
		this.customizeSetTypes(context);
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
		MenubarModule quickAccessMenu = this.applicationContext.getBean(QuickAccessMenuBarModule.class);
		modules.add(quickAccessMenu);
	}
	
	private void customizeSetTypes(CustomizationContext context)
	{
		Map<String, SetType> setTypes = this.getSetTypes(context);
		SetType quickAccess = new SetType(QUICKACCESS_ID);
		quickAccess.setKey("SET_TYPE_QUICKACCESS");
		quickAccess.setCustomizedClassLoader(this.getClass().getClassLoader());
		setTypes.put(quickAccess.getId(), quickAccess);
	}
	
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
