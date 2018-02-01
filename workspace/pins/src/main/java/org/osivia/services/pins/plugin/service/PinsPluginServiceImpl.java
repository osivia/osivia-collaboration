package org.osivia.services.pins.plugin.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.pins.plugin.repository.PinsPluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Pins plugin service implementation
 * @author jbarberet
 *
 */
@Service
public class PinsPluginServiceImpl implements PinsPluginService {
    
	private static final String PIN_LIST_WEBID = "pin:listwebid";

	/** Repository */
	@Autowired
	private PinsPluginRepository repository;
	
	/**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentDTO> getDocumentsList(PortalControllerContext portalControllerContext) throws PortletException
    {
		// Workspace document
        Document workspace = this.repository.getWorkspace(portalControllerContext);
        
        List<DocumentDTO> documents = new ArrayList<>();
        if (workspace != null)
        {
	        String workspacePath = workspace.getPath();
	
	        PropertyList list = (PropertyList) workspace.getProperties().get(PIN_LIST_WEBID);
	        if (list != null)
	        {
		        List<Object> listObject = list.list();
		        
		        documents = this.repository.getDocumentsList(portalControllerContext, workspacePath, listObject);
	        }
        }
        return documents;
    }
    
}
