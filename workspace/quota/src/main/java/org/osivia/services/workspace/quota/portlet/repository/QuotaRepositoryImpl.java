package org.osivia.services.workspace.quota.portlet.repository;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.quota.common.GetProcedureInstanceCommand;
import org.osivia.services.workspace.quota.common.GetQuotaCommand;
import org.osivia.services.workspace.quota.portlet.model.QuotaInformations;
import org.osivia.services.workspace.quota.portlet.model.QuotaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
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
        INuxeoCommand command = new GetQuotaCommand(path);

		long treeSize = 0;
		long trashedTreeSize = 0;
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
            trashedTreeSize = quotaContent.getLong("trashedtreesize");
            quota = quotaContent.getLong("quota");
         }
        
        QuotaInformations infos = new QuotaInformations(treeSize, trashedTreeSize, quota);
        
        return infos;
    }
    
    public Document getWorkspace(PortalControllerContext portalControllerContext) {
    	
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        // Display procedures information (if any)
        return nuxeoController.getDocumentContext(nuxeoController.getBasePath()).getDoc();
        //return doc.getProperties().getString("qtc:uuid");
    }
    
	@Override
	public Document getProcedureInfos(PortalControllerContext portalControllerContext) {
		Document ret = null;
    	
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        Document workspace = getWorkspace(portalControllerContext);
        String uuid = workspace.getProperties().getString("qtc:uuid");
        
        if(uuid != null) {

			nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
			nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
			nuxeoController.setForcePublicationInfosScope("superuser_context");
			
			GetProcedureInstanceCommand pic = new GetProcedureInstanceCommand(uuid);
        	Object executeNuxeoCommand = nuxeoController.executeNuxeoCommand(pic);
        	if(executeNuxeoCommand != null) {
        		ret = (Document) executeNuxeoCommand;        		
        	}
        }
        return ret;
	}
    
    
	@Override
	public List<QuotaItem> updateQuota(PortalControllerContext portalControllerContext, Long size) throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        String path = nuxeoController.getBasePath();

		// Nuxeo command
		INuxeoCommand command = this.applicationContext.getBean(UpdateQuotaCommand.class, path, size);

		nuxeoController.executeNuxeoCommand(command);

		return null;
	}

	@Override
	public Documents getBigFiles(PortalControllerContext portalControllerContext) {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        String path = nuxeoController.getBasePath();

		// Nuxeo command
		INuxeoCommand command = this.applicationContext.getBean(GetBigFilesCommand.class, path);

		Documents docs = (Documents) nuxeoController.executeNuxeoCommand(command);
		return docs;
	}


}
