package org.osivia.services.sets.quickaccess.plugin.menubar;

import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * QuickAccess plugin configuration
 * @author Julien Barberet
 *
 */
@Configuration
@ComponentScan(basePackages="org.osivia.services.sets.quickaccess.plugin")
public class QuickAccessMenuBarConfiguration {

	
    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
    
    
    /**
     * Constructor.
     */
	public QuickAccessMenuBarConfiguration() {
		super();
	}

	/**
	 * Get menubar service
	 * @return menubar service
	 */
	@Bean
	public IMenubarService getMenuBarService() {
		return Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
	}
	
	/**
	 * Get workspace service
	 * @return workspace service
	 */
	@Bean
	public WorkspaceService getWorkspaceService() {
		return DirServiceFactory.getService(WorkspaceService.class);
	}

    /**
     * Get portal URL factory.
     *
     * @return portal URL factory
     */
    @Bean
    public IPortalUrlFactory getPortalUrlFactory() {
        return Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
    }
    

    /**
     * Get internationalization bundle factory.
     *
     * @return internationalization bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader(), this.applicationContext);
    }
	
}
