package org.osivia.services.workspace.portlet.repository;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
    /** Member identifiers. */
    private final Set<String> members;


    /**
     * Constructor.
     *
     * @param modelPath model path
     * @param workspaceId workspace identifier
     */
    public GetInvitationsCommand(String modelPath, String workspaceId) {
        this(modelPath, workspaceId, null, null);
    }


    /**
     * Constructor.
     * 
     * @param modelPath model path
     * @param workspaceId workspace identifier
     * @param invitationState invitation state
     */
    public GetInvitationsCommand(String modelPath, String workspaceId, InvitationState invitationState) {
        this(modelPath, workspaceId, invitationState, null);
    }


    /**
     * Constructor.
     *
     * @param modelPath model path
     * @param workspaceId workspace identifier
     * @param invitationState invitation state
     * @param members member identifiers
     */
    public GetInvitationsCommand(String modelPath, String workspaceId, InvitationState invitationState, Set<String> members) {
        super();
        this.modelPath = modelPath;
        this.workspaceId = workspaceId;
        this.invitationState = invitationState;
        this.members = members;
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
        clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY).append(" = '").append(this.workspaceId)
                .append("' ");
        if (this.invitationState != null) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.INVITATION_STATE_PROPERTY).append(" = '")
                    .append(this.invitationState.toString()).append("' ");
        }
        if (this.members != null) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.PERSON_UID_PROPERTY).append(" IN (");
            boolean first = true;
            for (String member : this.members) {
                if (first) {
                    first = false;
                } else {
                    clause.append(", ");
                }

                clause.append("'").append(member).append("'");
            }
            clause.append(") ORDER BY dc:created DESC");
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
        builder.append("/");
        if (this.invitationState != null) {
            builder.append(this.invitationState.toString());
        }
        builder.append("/");
        if (this.members != null) {
            builder.append(StringUtils.join(this.members, ","));
        }
        return builder.toString();
    }

}
