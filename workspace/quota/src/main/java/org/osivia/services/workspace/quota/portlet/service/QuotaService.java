package org.osivia.services.workspace.quota.portlet.service;


import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.workspace.quota.portlet.model.AskQuotaForm;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;

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
	 * Refuse user request for quota
	 * 
	 * @param portalControllerContext
	 * @param form
	 * @throws PortletException
	 */
	void refuseQuota(PortalControllerContext portalControllerContext, UpdateForm form) throws PortletException;

   
	
    /**
     * Get form for quota ask
     * 
     * @param portalControllerContext
     * @return
     */
	AskQuotaForm getAskForm(PortalControllerContext portalControllerContext);

	/**
	 * Submit quota ask
	 * 
	 * @param portalControllerContext
	 * @param form
	 * @throws PortalException
	 * @throws FormFilterException
	 * @throws CMSException
	 */
	void ask(PortalControllerContext portalControllerContext, AskQuotaForm form) throws PortalException, FormFilterException, CMSException;


}
