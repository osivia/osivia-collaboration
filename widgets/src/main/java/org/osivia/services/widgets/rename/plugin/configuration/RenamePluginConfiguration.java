package org.osivia.services.widgets.rename.plugin.configuration;

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
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Rename plugin configuration.
 * 
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.widgets.rename.plugin")
public class RenamePluginConfiguration {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public RenamePluginConfiguration() {
        super();
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("rename");
        return messageSource;
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
     * Get menubar service.
     * 
     * @return menubar service
     */
    @Bean
    public IMenubarService getMenubarService() {
        return Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
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
