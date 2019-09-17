package org.osivia.services.workspace.quota.portlet.service;


import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateOptions;

import javax.portlet.PortletException;


// TODO: Auto-generated Javadoc
/**
 * Quota portlet service interface.
 *
 * @author Jean-Sébastien Steux
 */
public interface QuotaService {

    /**
     * Task identifier.
     */
    String TASK_ID = "QUOTA";


    /**
     * Get quota form.
     *
     * @param portalControllerContext portal controller context
     * @return quota form
     * @throws PortletException the portlet exception
     */
    QuotaForm getQuotaForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get update form.
     *
     * @param portalControllerContext portal controller context
     * @return quota form
     * @throws PortletException the portlet exception
     */
    UpdateForm getUpdateForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update user quota.
     *
     * @param portalControllerContext portal controller context
     * @param form                    update form
     * @throws PortletException the portlet exception
     */
    void updateQuota(PortalControllerContext portalControllerContext, UpdateForm form) throws PortletException;

    

    /**
     * Update options.
     *
     * @param portalControllerContext the portal controller context
     * @return the update options

     */
    
    UpdateOptions updateOptions(PortalControllerContext portalControllerContext) ;

   
}
