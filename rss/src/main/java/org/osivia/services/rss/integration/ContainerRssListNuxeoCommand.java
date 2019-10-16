package org.osivia.services.rss.integration;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoCompatibility;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * List Nuxeo events command.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 * @see INuxeoCommand
 */
public class ContainerRssListNuxeoCommand implements INuxeoCommand {

    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;

    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     */
    public ContainerRssListNuxeoCommand(NuxeoQueryFilterContext queryContext) {
        super();
        this.queryContext = queryContext;
    }    
    
    @Override
	public Object execute(Session nuxeoSession) throws Exception {
    
    	// Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'RssContainer' ");

        // Filter on published documents
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(this.queryContext, clause.toString());

        // Request
        OperationRequest request;
        if (NuxeoCompatibility.canUseES()) {
            request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "*");
        } else {
            request = nuxeoSession.newRequest("Document.Query");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        }
        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return request.execute();
	}    

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}    
    
}
