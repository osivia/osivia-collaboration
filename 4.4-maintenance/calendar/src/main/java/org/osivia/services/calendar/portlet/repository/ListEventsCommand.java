package org.osivia.services.calendar.portlet.repository;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
public class ListEventsCommand implements INuxeoCommand {

    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;
    /** Context path. */
    private final String contextPath;
    /** Start date. */
    private final Date startDate;
    /** End date. */
    private final Date endDate;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public ListEventsCommand(NuxeoQueryFilterContext queryContext, String contextPath, Date startDate, Date endDate) {
        super();
        this.queryContext = queryContext;
        this.contextPath = contextPath;
        this.startDate = startDate;
        this.endDate = endDate;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        String start = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(this.startDate);

        String end;
        if (this.endDate == null) {
            end = null;
        } else {
            end = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(this.endDate);
        }

        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:mixinType = 'Schedulable' ");
        clause.append("AND ecm:path STARTSWITH '").append(this.contextPath).append("' ");
        if (StringUtils.isNotEmpty(end)) {
            clause.append("AND (vevent:dtstart < TIMESTAMP '").append(end).append("') ");
        }
        clause.append("AND (vevent:dtend > TIMESTAMP '").append(start).append("') ");
        clause.append("ORDER BY vevent:dtstart");

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
        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Calendar/" + this.contextPath;
    }

}
