package org.osivia.services.calendar.view.portlet.configuration;

import javax.annotation.PostConstruct;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.portlet.context.PortletContextAware;
import org.springframework.web.portlet.multipart.CommonsPortletMultipartResolver;
import org.springframework.web.portlet.multipart.PortletMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;

/**
 * Calendar view portlet configuration.
 *
 * @author Julien Barberet
 * @see CMSPortlet
 * @see PortletContextAware
 */
@Configuration
@ComponentScan(basePackages = {"org.osivia.services.calendar.view.portlet", "org.osivia.services.calendar.common"})
public class CalendarViewConfiguration extends CMSPortlet implements PortletContextAware {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;


    /**
     * Constructor.
     */
    public CalendarViewConfiguration() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        portletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.applicationContext);
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
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
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
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class, IInternationalizationService
                .MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader(), this.applicationContext);
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
    

    /**
     * Get Nuxeo service.
     *
     * @return Nuxeo service
     */
    @Bean
    public INuxeoService getNuxeoService() {
        return Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
    }

    
    /**
     * Get menuBar service.
     * 
     * @return menuBar service
     */
    @Bean
    public IMenubarService getMenuBarService() {
        return Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
    }
}
