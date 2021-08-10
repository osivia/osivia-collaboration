package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import java.util.Iterator;
import java.util.List;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get FileBrowser SubFolders Content Command
 * 
 * @author Loïc Billon
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetFileBrowserSubFoldersContentCommand implements INuxeoCommand {

    /** Parent document identifier. */
    private List<String> subpaths;


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Nuxeo request
        StringBuilder nuxeoRequest = new StringBuilder();
        nuxeoRequest.append("ecm:mixinType IN ('Folderish', 'Audio', 'Video', 'Picture', 'Downloadable') AND (");
        
        Iterator<String> iterator = subpaths.iterator();
        while(iterator.hasNext()) {
        	nuxeoRequest.append("(ecm:path STARTSWITH '").append(iterator.next()).append("')");
        	
        	if(!iterator.hasNext() ) {
        		nuxeoRequest.append(") ");
        	}
        	else {
        		nuxeoRequest.append(" OR ");
        	}
        }  
        nuxeoRequest.append(" ORDER BY ecm:path");

        // Query filter
        NuxeoQueryFilterContext queryFilterContext = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE,
                InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_NO_FILTER);
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, nuxeoRequest.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("Document.QueryES");
        operationRequest.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return operationRequest.execute();
    }

	public void setSubpaths(List<String> subpaths) {
		this.subpaths = subpaths;
	}


	/**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
