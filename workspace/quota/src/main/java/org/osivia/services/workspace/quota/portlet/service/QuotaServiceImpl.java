package org.osivia.services.workspace.quota.portlet.service;

import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.HtmlFormatter;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.model.QuotaInformations;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateOptions;
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

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();


        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // quota form
        QuotaForm form = this.applicationContext.getBean(QuotaForm.class);

        QuotaInformations infos = this.repository.getQuotaItems(portalControllerContext);
        String sizeMessage = HtmlFormatter.formatSize(locale, bundle, infos.getTreeSize()) + " "+bundle.getString("QUOTA_USED") ;
        int ratio = 0;

        if (infos.getQuota() != -1) {
            sizeMessage += " " + bundle.getString("QUOTA_OVER") + " " + HtmlFormatter.formatSize(locale, bundle, infos.getQuota());

            ratio = (int) ((infos.getTreeSize() * 100) / infos.getQuota());
        }

        form.setSizeMessage(sizeMessage);
        form.setRatio(ratio);

        form.setInfos(infos);

        Long updateNav = (Long) request.getAttribute(Constants.PORTLET_ATTR_UPDATE_SPACE_DATA_TS);

        form.setAsynchronous(false);
        form.setTs(System.currentTimeMillis());

        if (request instanceof RenderRequest) {
            if (updateNav != null) {

                // On considère que pendant 10s. après l'évènement d'update la valeur de quota peut changer
                // (La mise à jour depuis la corbeille est par exemple asynchrone)
                // On joue donc un chargement asynchrone
                
                if (System.currentTimeMillis() - updateNav < 10000) {

                    form.setAsynchronous(true);

                    // Ne pas stocker dans le cache partagé
                    request.setAttribute("osivia.invalidateSharedCache", "1");
                }
            }
        }

        return form;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateQuota(PortalControllerContext portalControllerContext, UpdateForm form) throws PortletException {
       this.repository.updateQuota(portalControllerContext, Long.parseLong(form.getSize()) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

    @Override
    public UpdateForm getUpdateForm(PortalControllerContext portalControllerContext) throws PortletException {

        UpdateForm form = this.applicationContext.getBean(UpdateForm.class);
        return form;
    }

    @Override
    public UpdateOptions updateOptions(PortalControllerContext portalControllerContext) {
        UpdateOptions  options = new UpdateOptions();
        options.getSizes().add("1");
        options.getSizes().add("10");        
        options.getSizes().add("100");
        options.getSizes().add("1000");      
        return options;
    }

}
