package org.osivia.services.rss.container.portlet.configuration;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
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
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

@Configuration
@ComponentScan(basePackages = {"org.osivia.services.rss.container.portlet", "org.osivia.services.rss.common"})
public class RssContainerConfiguration extends CMSPortlet implements PortletConfigAware {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


	@Override
	public void setPortletConfig(PortletConfig portletConfig) {
		try {
			super.init(portletConfig);
		} catch (PortletException e) {
			throw new RuntimeException(e);
		}
		PortletAppUtils.registerApplication(portletConfig, applicationContext);
	}


    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/containers/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = {"messageSource"})
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }


	/**
	 * Get internationalization bundle factory.
	 *
	 * @return internationalization bundle factory
	 */
	@Bean
	public IBundleFactory getBundleFactory() {
		IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class, IInternationalizationService.MBEAN_NAME);
		return internationalizationService.getBundleFactory(getClass().getClassLoader());
	}


	/**
	 * Get notifications service.
	 *
	 * @return notifications service
	 */
	@Bean
	public INotificationsService getNotificationsService() {
		return Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
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
	 * @return document DAO
	 */
	@Bean
	public DocumentDAO getDocumentDao() {
		return DocumentDAO.getInstance();
	}

}
