package org.osivia.services.rss.container.portlet.configuration;

import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan(basePackages={"org.osivia.services.rss.container.portlet"})
public class RssContainerConfiguration {

	  @Bean
	  public InternalResourceViewResolver getViewResolver()
	  {
	    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	    viewResolver.setCache(true);
	    viewResolver.setViewClass(JstlView.class);
	    viewResolver.setPrefix("/WEB-INF/jsp/");
	    viewResolver.setSuffix(".jsp");
	    return viewResolver;
	  }
	  
	  @Bean(name={"messageSource"})
	  public ResourceBundleMessageSource getMessageSource()
	  {
	    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("Resource");
	    return messageSource;
	  }
	  
	  @Bean
	  public IBundleFactory getBundleFactory()
	  {
	    IInternationalizationService internationalizationService = (IInternationalizationService)Locator.findMBean(IInternationalizationService.class, "osivia:service=InternationalizationService");
	    
	    return internationalizationService.getBundleFactory(getClass().getClassLoader());
	  }
	  
	  @Bean
	  public INotificationsService getNotificationService()
	  {
	    return (INotificationsService)Locator.findMBean(INotificationsService.class, "osivia:service=NotificationsService");
	  }	
	
}
