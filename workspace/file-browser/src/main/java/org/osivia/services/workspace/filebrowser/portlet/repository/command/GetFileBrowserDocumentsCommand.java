package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Get file browser documents Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetFileBrowserDocumentsCommand implements INuxeoCommand {

    /**
     * NXQL request.
     */
    private final String nxql;
    /**
     * Parent document identifier.
     */
    private final String parentId;


    /**
     * Constructor.
     *
     * @param nxql       NXQL request
     * @param parentId parent document identifier
     */
    public GetFileBrowserDocumentsCommand(String nxql, String parentId) {
        super();
        this.nxql = nxql;
        this.parentId = parentId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Nuxeo request
        StringBuilder nuxeoRequest = new StringBuilder();

        nuxeoRequest.append("NOT ecm:primaryType IN ('Workspace', 'WorkspaceRoot', 'PortalSite', 'Favorites') ");
        if (StringUtils.isEmpty(this.nxql)) {
            nuxeoRequest.append("AND ecm:parentId = '").append(this.parentId).append("' ");
        } else {
            nuxeoRequest.append("AND ");
            nuxeoRequest.append(this.nxql);
        }

        // Query filter
        NuxeoQueryFilterContext queryFilterContext = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE_N_PUBLISHED,
                InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_NO_FILTER);
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(queryFilterContext, nuxeoRequest.toString());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("Document.QueryES");
        operationRequest.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        operationRequest.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return operationRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "GetFileBrowserDocumentsCommand";
    }

}
