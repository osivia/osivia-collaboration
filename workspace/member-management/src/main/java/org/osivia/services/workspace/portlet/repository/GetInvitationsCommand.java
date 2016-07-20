package org.osivia.services.workspace.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get invitations command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetInvitationsCommand implements INuxeoCommand {

    /** Model path. */
    private final String modelPath;
    /** Workspace identifier. */
    private final String workspaceId;
    /** Invitation state. */
    private final InvitationState invitationState;


    /**
     * Constructor.
     *
     * @param modelPath model path
     * @param workspaceId workspace identifier
     */
    public GetInvitationsCommand(String modelPath, String workspaceId) {
        this(modelPath, workspaceId, null);
    }


    /**
     * Constructor.
     *
     * @param modelPath model path
     * @param workspaceId workspace identifier
     */
    public GetInvitationsCommand(String modelPath, String workspaceId, InvitationState invitationState) {
        super();
        this.modelPath = modelPath;
        this.workspaceId = workspaceId;
        this.invitationState = invitationState;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:procedureModelPath = '").append(this.modelPath).append("' ");
        clause.append("AND pi:globalVariablesValues.workspaceId = '").append(this.workspaceId).append("' ");
        if (this.invitationState != null) {
            clause.append("AND pi:globalVariablesValues.invitationState = '").append(this.invitationState.toString()).append("' ");
        }

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.modelPath);
        builder.append("/");
        builder.append(this.workspaceId);
        if (this.invitationState != null) {
            builder.append("/");
            builder.append(this.invitationState.toString());
        }
        return builder.toString();
    }

}
