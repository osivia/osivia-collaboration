package org.osivia.services.workspace.quota.portlet.service;


import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;


import javax.portlet.PortletException;


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
     * @return trash form
     */
    QuotaForm getQuotaForm(PortalControllerContext portalControllerContext) throws PortletException;

 

    /**
     * Update user quota
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void updateQuota(PortalControllerContext portalControllerContext, QuotaForm form) throws PortletException;

   
}
