package org.osivia.services.workspace.quota.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.quota.portlet.model.QuotaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Quota repository implementation.
 *
 * @author Jean-Sébastien Steux
 * @see QuotaRepository
 */
@Repository
public class QuotaRepositoryImpl implements QuotaRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public QuotaRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuotaItem> getQuotaItems(PortalControllerContext portalControllerContext) {
       
        List<QuotaItem> items = new ArrayList<>();
        
        return items;
    }




    @Override
    public List<QuotaItem> updateQuota(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateQuotaCommand.class, portalControllerContext.getRequest().getRemoteUser());

        nuxeoController.executeNuxeoCommand(command);

        return null;
    }



}
