/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import javax.portlet.PortletContext;

import org.osivia.portal.api.PortalException;

/**
 * @author Loïc Billon
 *
 */
public interface QuotaReportingService {

	/**
	 * @param portletContext
	 * @throws PortalException 
	 */
	void installBatch(PortletContext portletContext) throws PortalException;

	/**
	 * 
	 */
	void uninstallBatch();

}
