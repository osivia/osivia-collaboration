package org.osivia.services.rss.batch;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.multipart.CommonsPortletMultipartResolver;
import org.springframework.web.portlet.multipart.PortletMultipartResolver;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

@Configuration
@ComponentScan(basePackages={"org.osivia.services.rss.common", "org.osivia.services.rss.batch"})
public class BatchRssConfiguration extends CMSPortlet implements PortletConfigAware {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
  
	@Bean(name = { "messageSource" })
	public ResourceBundleMessageSource getMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("Resource");
		return messageSource;
	}

	@Bean
	public IBundleFactory getBundleFactory() {
		IInternationalizationService internationalizationService = (IInternationalizationService) Locator
				.findMBean(IInternationalizationService.class, "osivia:service=InternationalizationService");

		return internationalizationService.getBundleFactory(getClass().getClassLoader());
	}

	@Bean
	public INotificationsService getNotificationService() {
		return (INotificationsService) Locator.findMBean(INotificationsService.class,
				"osivia:service=NotificationsService");
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
     * Get document DAO.
     *
     * @return DAO
     */
    @Bean
    public DocumentDAO getDocumentDao() {
        return DocumentDAO.getInstance();
    }
    
    /**
     * Get multipart resolver.
     *
     * @return multipart resolver
     */
    @Bean(name = "portletMultipartResolver")
    public PortletMultipartResolver getMultipartResolver() {
        CommonsPortletMultipartResolver multipartResolver = new CommonsPortletMultipartResolver();
        multipartResolver.setDefaultEncoding(CharEncoding.UTF_8);
        multipartResolver.setMaxUploadSizePerFile(2 * FileUtils.ONE_MB);
        return multipartResolver;
    }    
}
