package org.osivia.services.rss.container.portlet.configuration;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

@Configuration
@ComponentScan(basePackages={"org.osivia.services.rss.container.portlet", "org.osivia.services.rss.common"})
public class RssContainerConfiguration extends CMSPortlet implements PortletConfigAware {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

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

	@Override
	public void setPortletConfig(PortletConfig portletConfig) {
		try {
			super.init(portletConfig);
		} catch (PortletException e) {
			throw new RuntimeException(e);
		}
		PortletAppUtils.registerApplication(portletConfig, applicationContext);
	}
	
}
