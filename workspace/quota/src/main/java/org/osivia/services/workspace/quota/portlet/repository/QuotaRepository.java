package org.osivia.services.workspace.quota.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
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
     * @return quota items
     * @throws PortletException
     */
	QuotaInformations getQuotaItems(PortalControllerContext portalControllerContext) throws PortletException;

	/**
	 * Get procedure information if exists
	 * @param portalControllerContext
	 * @return curentStep of procedure or null
	 */
	Document getProcedureInfos(PortalControllerContext portalControllerContext);
	
	
    /**
     * Update user quota
     * 
     * @param portalControllerContext portal controller context
     * @return rejected documents
     * @throws PortletException
     */
    List<QuotaItem> updateQuota(PortalControllerContext portalControllerContext, Long size) throws PortletException;

    /**
     * List big files in repository
     * 
     * @param portalControllerContext
     * @return
     */
	Documents getBigFiles(PortalControllerContext portalControllerContext);

	/**
	 * Get current workspace document 
	 * 
	 * @param portalControllerContext
	 * @return
	 */
	Document getWorkspace(PortalControllerContext portalControllerContext);




}
