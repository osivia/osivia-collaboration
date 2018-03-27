package org.osivia.services.calendar.view.portlet.repository.command;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoCompatibility;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * List Nuxeo events command.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class EventListCommand implements INuxeoCommand {

    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;
    /** Context path. */
    private final String contextPath;
    /** Start date. */
    private final Date startDate;
    /** End date. */
    private final Date endDate;
    
    private final String sourcesId;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public EventListCommand(NuxeoQueryFilterContext queryContext, String contextPath, Date startDate, Date endDate, List<CalendarSynchronizationSource> listSource) {
        super();
        this.queryContext = queryContext;
        this.contextPath = contextPath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sourcesId = getStringValues(listSource);
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
        clause.append("AND (sync:idParentSource is null or sync:idParentSource = '' ");
        if (this.sourcesId != null && !this.sourcesId.isEmpty()) clause.append(" OR sync:idParentSource IN (").append(this.sourcesId).append(")");
        clause.append(") ");
        clause.append("ORDER BY vevent:dtstart");

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

    /**
     * Get string values.
     * 
     * @param values values
     * @return string
     */
    private String getStringValues(List<CalendarSynchronizationSource> values) {
        String result;

        if (values == null) {
            result = null;
        } else {
            StringBuilder builder = new StringBuilder();

            boolean first = true;
            for (CalendarSynchronizationSource value : values) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }

                builder.append("'");
                builder.append(value.getId());
                builder.append("'");
            }

            result = builder.toString();
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Calendar/" + this.contextPath;
    }

}
