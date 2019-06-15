package org.osivia.services.workspace.quota.portlet.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.quota.portlet.model.QuotaInformations;
import org.osivia.services.workspace.quota.portlet.model.QuotaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import net.sf.json.JSONObject;

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
    public QuotaInformations getQuotaItems(PortalControllerContext portalControllerContext) throws PortletException {
    	
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
    	
        String path = nuxeoController.getBasePath();
        INuxeoCommand command = this.applicationContext.getBean(GetQuotaCommand.class, path);

		long treeSize = 0;
		long quota = 0;
       
        
		Blob quotaInfos =  (Blob) nuxeoController.executeNuxeoCommand(command);        
		
        if (quotaInfos != null) {
            
            String quotaInfosContent;
			try {
				quotaInfosContent = IOUtils.toString(quotaInfos.getStream(), "UTF-8");
			} catch (IOException e) {
				throw new PortletException( e);
			}

            JSONObject quotaContent = JSONObject.fromObject(quotaInfosContent);
            
            treeSize = quotaContent.getLong("treesize");
            quota = quotaContent.getLong("quota");
         }
        
        QuotaInformations infos = new QuotaInformations(treeSize, quota);
        return infos;
    }

    
    
	@Override
	public List<QuotaItem> updateQuota(PortalControllerContext portalControllerContext) throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

		// Nuxeo command
		INuxeoCommand command = this.applicationContext.getBean(UpdateQuotaCommand.class,
				portalControllerContext.getRequest().getRemoteUser());

		nuxeoController.executeNuxeoCommand(command);

		return null;
	}

}
