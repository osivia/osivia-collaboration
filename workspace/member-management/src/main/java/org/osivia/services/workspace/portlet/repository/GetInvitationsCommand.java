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
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Get invitations Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetInvitationsCommand implements INuxeoCommand {

    /** Workspace identifier. */
    private final String workspaceId;
    /** Model identifier. */
    private final String modelId;
    /** Invitation state. */
    private final InvitationState invitationState;
    /** User identifiers. */
    private final Set<String> identifiers;


    /**
     * Constructor.
     *
     * @param workspaceId workspace identifier
     */
    public GetInvitationsCommand(String workspaceId) {
        this(workspaceId, false, null, null);
    }

    /**
     * Constructor.
     *
     * @param workspaceId workspace identifier
     * @param request invitation request indicator
     */
    public GetInvitationsCommand(String workspaceId, boolean request) {
        this(workspaceId, request, null, null);
    }

    /**
     * Constructor.
     * 
     * @param workspaceId workspace identifier
     * @param invitationState invitation state
     */
    public GetInvitationsCommand(String workspaceId, InvitationState invitationState) {
        this(workspaceId, false, invitationState, null);
    }

    /**
     * Constructor.
     * 
     * @param workspaceId workspace identifier
     * @param request invitation request indicator
     * @param invitationState invitation state
     */
    public GetInvitationsCommand(String workspaceId, boolean request, InvitationState invitationState) {
        this(workspaceId, request, invitationState, null);
    }

    /**
     * Constructor.
     *
     * @param workspaceId workspace identifier
     * @param invitationState invitation state
     * @param identifiers user identifiers
     */
    public GetInvitationsCommand(String workspaceId, InvitationState invitationState, Set<String> identifiers) {
        this(workspaceId, false, invitationState, identifiers);
    }

    /**
     * Contructor.
     * 
     * @param workspaceId workspace identifier
     * @param request invitation request indicator
     * @param identifiers user identifiers
     */
    public GetInvitationsCommand(String workspaceId, boolean request, Set<String> identifiers) {
        this(workspaceId, request, null, identifiers);
    }

    /**
     * Constructor.
     *
     * @param workspaceId workspace identifier
     * @param request invitation request indicator
     * @param invitationState invitation state
     * @param identifiers user identifiers
     */
    public GetInvitationsCommand(String workspaceId, boolean request, InvitationState invitationState, Set<String> identifiers) {
        super();
        this.workspaceId = workspaceId;
        if (request) {
            this.modelId = MemberManagementRepository.REQUEST_MODEL_ID;
        } else {
            this.modelId = MemberManagementRepository.INVITATION_MODEL_ID;
        }
        this.invitationState = invitationState;
        this.identifiers = identifiers;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:procedureModelWebId = '").append(IFormsService.FORMS_WEB_ID_PREFIX).append(this.modelId).append("' ");
        clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY).append(" = '").append(this.workspaceId)
                .append("' ");
        if (this.invitationState != null) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.INVITATION_STATE_PROPERTY).append(" = '")
                    .append(this.invitationState.toString()).append("' ");
        }
        if (this.identifiers != null) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.PERSON_UID_PROPERTY).append(" IN (");
            boolean first = true;
            for (String member : this.identifiers) {
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
        builder.append(this.workspaceId);
        builder.append("/");
        builder.append(this.modelId);
        builder.append("/");
        if (this.invitationState != null) {
            builder.append(this.invitationState.toString());
        }
        builder.append("/");
        if (this.identifiers != null) {
            builder.append(StringUtils.join(this.identifiers, ","));
        }
        return builder.toString();
    }

}
