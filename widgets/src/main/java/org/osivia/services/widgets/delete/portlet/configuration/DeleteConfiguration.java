package org.osivia.services.widgets.delete.portlet.configuration;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

/**
 * Delete portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.widgets.delete.portlet")
public class DeleteConfiguration extends CMSPortlet implements PortletConfigAware {

    /**
     * Log.
     */
    private final Log log;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DeleteConfiguration() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());
    }


    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            this.log.error(e.getMessage(), e.getCause());
        }

        PortletAppUtils.registerApplication(portletConfig, this.applicationContext);
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
        viewResolver.setPrefix("/WEB-INF/jsp/delete/");
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
        messageSource.setBasename("delete");
        return messageSource;
    }


    /**
     * Get CMS service locator.
     *
     * @return CMS service locator
     */
    @Bean
    public ICMSServiceLocator getCmsServiceLocator() {
        return Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
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
     * Get document DAO.
     *
     * @return document DAO
     */
    @Bean
    public DocumentDAO getDocumentDao() {
        return DocumentDAO.getInstance();
    }

}
