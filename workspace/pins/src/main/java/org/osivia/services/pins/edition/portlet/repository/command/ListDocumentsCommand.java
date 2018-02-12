package org.osivia.services.pins.edition.portlet.repository.command;

import java.util.Iterator;
import java.util.List;

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
 * @see INuxeoCommand
 */
public class ListDocumentsCommand implements INuxeoCommand {

    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;
    /** List of webid */
    private List<Object> listWebid;
    /** Workspace Uid */
    private String workspacePath;
    
    private boolean withDeleted;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public ListDocumentsCommand(NuxeoQueryFilterContext queryContext, List<Object> listWebid, String workspacePath, boolean withDeleted) {
        super();
        this.queryContext = queryContext;
        this.listWebid = listWebid;
        this.workspacePath = workspacePath;
        this.withDeleted = withDeleted;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:path startswith '").append(workspacePath).append("' ");
        
        if (listWebid != null && listWebid.size() > 0)
        {
        	clause.append(" and ttc:webid in (");
        	Iterator<Object> it = listWebid.iterator();
        	boolean first = true;
        	while (it.hasNext())
        	{
        		if (!first) clause.append(",");
        		clause.append("'").append(it.next()).append("'");
        		first = false;
        	}
        	clause.append(") ");
        }

        // Filter on published documents
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(this.queryContext, clause.toString());

        // Request
        OperationRequest request;
        if (NuxeoCompatibility.canUseES()) {
            request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice, vevent");
        } else {
            request = nuxeoSession.newRequest("Document.Query");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, common, toutatice, vevent");
        }
        request.set("query", "SELECT * FROM Document WHERE " + (withDeleted? clause.toString() : filteredRequest));

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Pins/" + this.workspacePath + listWebid;
    }

}
