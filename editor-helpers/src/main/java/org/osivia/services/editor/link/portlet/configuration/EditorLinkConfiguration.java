package org.osivia.services.editor.link.portlet.configuration;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.osivia.portal.core.web.IWebIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.swing.text.html.HTMLDocument;

/**
 * Editor link portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.editor.link.portlet")
public class EditorLinkConfiguration extends CMSPortlet implements PortletConfigAware {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public EditorLinkConfiguration() {
        super();
    }


    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }

        // Register portlet application context
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
        viewResolver.setPrefix("/WEB-INF/jsp/link/");
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
        messageSource.setBasenames("editor-link", "editor-link-validation");
        return messageSource;
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
     * Get webId service.
     *
     * @return webId service
     */
    @Bean
    public IWebIdService getWebIdService() {
        return Locator.findMBean(IWebIdService.class, IWebIdService.MBEAN_NAME);
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
     * Get document DAO.
     *
     * @return DAO
     */
    @Bean
    public DocumentDAO getDocumentDao() {
        return DocumentDAO.getInstance();
    }

}
