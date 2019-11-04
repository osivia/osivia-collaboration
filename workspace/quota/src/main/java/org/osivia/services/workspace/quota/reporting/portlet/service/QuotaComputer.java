/**
 * 
 */
package org.osivia.services.workspace.quota.reporting.portlet.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.html.HtmlFormatter;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.workspace.quota.common.GetProcedureInstanceCommand;
import org.osivia.services.workspace.quota.common.GetQuotaCommand;
import org.osivia.services.workspace.quota.common.UpdateQuotaComputationCommand;

import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import net.sf.json.JSONObject;

/**
 * Batch that computes quotas (min every day)
 * 
 * @author Loïc Billon
 *
 */
public class QuotaComputer extends NuxeoBatch {

	private final double threshold;

	private final static Log logger = LogFactory.getLog("batch");
	private static PortletContext portletContext;

	public QuotaComputer() {
		
		String thresholdParam = System.getProperty("osivia.services.quota.threshold");
		
		if(StringUtils.isNotBlank(thresholdParam)) {
			
			threshold = Double.parseDouble(thresholdParam);
		}
		else {
			threshold = 0.8;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osivia.portal.api.batch.AbstractBatch#getJobScheduling()
	 */
	@Override
	public String getJobScheduling() {
		
		String quotaCron = System.getProperty("osivia.services.quota.cron");
		
		if(StringUtils.isNotBlank(quotaCron)) {
			return quotaCron;
		}
		else return "0 0/10 * * * ?";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osivia.portal.api.batch.AbstractBatch#execute(java.util.Map)
	 */
	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {

		Documents workspaces = (Documents) getNuxeoController().executeNuxeoCommand(new QuotaSearchWksCommand());

		// for each workspace to update
		for (Document workspace : workspaces) {
			Blob quotaInfos = (Blob) getNuxeoController().executeNuxeoCommand(new GetQuotaCommand(workspace.getPath()));

			try {
				// get current quota
				if (quotaInfos != null) {

					String quotaInfosContent;

					quotaInfosContent = IOUtils.toString(quotaInfos.getStream(), "UTF-8");

					JSONObject quotaContent = JSONObject.fromObject(quotaInfosContent);

					double treeSize = new Double(quotaContent.getLong("treesize"));
					double quota = new Double(quotaContent.getLong("quota"));

					logger.warn("Quota : " + workspace.getTitle() + " (" + treeSize + "/" + quota + ")");

					boolean quotaExceeded = treeSize / quota > threshold;

					// If this workspace is currently in a procedure
					String uuid = workspace.getProperties().getString("qtc:uuid");
					if (uuid != null) {
						Object piObject = getNuxeoController().executeNuxeoCommand(new GetProcedureInstanceCommand(uuid));
						
						if(piObject != null) {
							
							Document pi = (Document) piObject;
	
							String currentStep = pi.getProperties().getString("pi:currentStep");
	
							// State warning
							if (currentStep.contentEquals("warning")) {
	
								// - if problem is not solved, update the current size
								Map<String, String> variables = new HashMap<String, String>();
								if (quotaExceeded) {
									addQuotaInfo(quotaContent, treeSize, quota, variables);

									proceed(uuid, "updateWarning", variables);
									
								}
								// - if problem is solved, delete the procedure
								else {
									proceed(uuid, "stopWarning", variables);
									uuid = null;
	
								}
							}
	
							// State request - do nothing
	
							// State resolved - check quota
						} else {
							uuid = null; // If wrong procedureId is still in workspace, renew procedure.
						}

					}

					// control the quota threshold
					if (quotaExceeded && uuid == null) {
						Map<String, String> variables = new HashMap<String, String>();

						variables.put("documentPath", workspace.getPath());
						variables.put("workspaceId", workspace.getProperties().getString("webc:url"));
						
						addQuotaInfo(quotaContent, treeSize, quota, variables);

						variables = startProcedure("quota_exceeding", variables);
						uuid = variables.get("uuid");

					}
					
					getNuxeoController().executeNuxeoCommand(
							new UpdateQuotaComputationCommand(workspace, treeSize, uuid));


				}
			} catch (IOException e) {
				throw new PortalException(e);
			} catch (FormFilterException e) {
				throw new PortalException(e);
			} catch (CMSException e) {
				throw new PortalException(e);
			}
		}
	}

	private void addQuotaInfo(JSONObject quotaContent, double treeSize, double quota, Map<String, String> variables) {
		Double d = new Double((treeSize / quota)*100);
		String format = new DecimalFormat("#.#").format(d);
		variables.put("spaceUsedInPercent",  format + "%");
		
		IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class, IInternationalizationService.MBEAN_NAME);
		IBundleFactory bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
		
		Bundle bundle = bundleFactory.getBundle(Locale.getDefault());
		
		String formatSize = HtmlFormatter.formatSize(Locale.getDefault(), bundle, quotaContent.getLong("quota"));
		variables.put("quota", formatSize);
	}

	public void setPortletContext(PortletContext portletContext) {
		QuotaComputer.portletContext = portletContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.toutatice.portail.cms.nuxeo.api.batch.FormBatch#getPortletContext()
	 */
	@Override
	protected PortletContext getPortletContext() {
		return QuotaComputer.portletContext;
	}
}
