package org.osivia.services.widgets.delete.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Get remote proxies Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetProxiesCommand implements INuxeoCommand {

    /**
     * WebIds.
     */
    private final Set<String> webIds;


    /**
     * Constructor.
     *
     * @param webIds webIds
     */
    public GetProxiesCommand(Set<String> webIds) {
        super();
        this.webIds = webIds;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Nuxeo request
        StringBuilder nuxeoRequest = new StringBuilder();
        nuxeoRequest.append("ttc:webid IN ('");
        nuxeoRequest.append(StringUtils.join(this.webIds, "', '"));
        nuxeoRequest.append("')");

        // Query filter
        NuxeoQueryFilterContext queryFilterContext = NuxeoQueryFilterContext.CONTEXT_DEFAULT;
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, nuxeoRequest.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("Document.QueryES");
        operationRequest.setHeader(Constants.HEADER_NX_SCHEMAS, "common, dublincore, toutatice");
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return operationRequest.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
