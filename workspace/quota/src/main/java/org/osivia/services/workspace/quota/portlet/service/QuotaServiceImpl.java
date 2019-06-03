package org.osivia.services.workspace.quota.portlet.service;

import java.util.Locale;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.repository.QuotaRepository;
import org.osivia.services.workspace.quota.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Quota portlet service implementation.
 *
 * @author Jean-Sébastien Steux
 * @see QuotaService
 * @see ApplicationContextAware
 */
@Service
public class QuotaServiceImpl implements QuotaService, ApplicationContextAware {

    /**
     * Portlet repository.
     */
    @Autowired
    private QuotaRepository repository;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Application context.
     */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public QuotaServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaForm getQuotaForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Trash form
        QuotaForm form = this.applicationContext.getBean(QuotaForm.class);

        if (!form.isLoaded()) {
            // Trashed documents
//            List<TrashedDocument> trashedDocuments = this.repository.getTrashedDocuments(portalControllerContext);
//            form.setTrashedDocuments(trashedDocuments);
//            
//            form.setLoaded(true);
        }

        return form;
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void updateQuota(PortalControllerContext portalControllerContext, QuotaForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);
        
        this.repository.updateQuota(portalControllerContext);

        // Service invocation
//        List<TrashedDocument> rejected = this.repository.updateQuota(portalControllerContext);
//
//        // Update model
//        this.updateModel(portalControllerContext, form, null, rejected, bundle, "TRASH_RESTORE_ALL_MESSAGE_");
    }





    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

}
