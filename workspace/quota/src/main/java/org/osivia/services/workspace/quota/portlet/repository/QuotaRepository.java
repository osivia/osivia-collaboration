package org.osivia.services.workspace.quota.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.quota.portlet.model.QuotaInformations;
import org.osivia.services.workspace.quota.portlet.model.QuotaItem;

/**
 * Quota portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface QuotaRepository {

    /**
     * Get quota items
     * 
     * @param portalControllerContext portal controller context
     * @return trashed documents
     * @throws PortletException
     */
	QuotaInformations getQuotaItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update user quota
     * 
     * @param portalControllerContext portal controller context
     * @return rejected documents
     * @throws PortletException
     */
    List<QuotaItem> updateQuota(PortalControllerContext portalControllerContext) throws PortletException;


}
