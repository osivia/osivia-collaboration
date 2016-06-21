package org.osivia.services.workspace.configuration;

import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Workspace creation configuration.
 *
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.workspace")
public class WorkspaceCreationConfiguration {

    /**
     * Constructor.
     */
    public WorkspaceCreationConfiguration() {
        super();
    }


    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }


    /**
     * Get workspace service.
     *
     * @return workspace service
     */
    @Bean
    public WorkspaceService getWorkspaceService() {
        return DirServiceFactory.getService(WorkspaceService.class);
    }


    /**
     * Get taskbar service.
     * 
     * @return taskbar service
     */
    @Bean
    public ITaskbarService getTaskbarService() {
        return Locator.findMBean(ITaskbarService.class, ITaskbarService.MBEAN_NAME);
    }


    /**
     * Get bundle factory.
     *
     * @return bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * Get notifications service.
     * 
     * @return notification service
     */
    @Bean
    public INotificationsService getNotificationService() {
        return Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
    }

}
