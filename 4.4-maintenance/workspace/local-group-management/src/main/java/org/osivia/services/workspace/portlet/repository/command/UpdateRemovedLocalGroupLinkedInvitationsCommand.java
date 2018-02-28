package org.osivia.services.workspace.portlet.repository.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Update removed local group linked invitations Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateRemovedLocalGroupLinkedInvitationsCommand implements INuxeoCommand {

    /** Invitation model identifier. */
    private static final String MODEL_ID = "invitation";

    /** Global variables Nuxeo document property. */
    private static final String GLOBAL_VARIABLES_PROPERTY = "pi:globalVariablesValues";
    /** Workspace identifier Nuxeo document property. */
    private static final String WORKSPACE_IDENTIFIER_PROPERTY = "workspaceId";
    /** Invitation state Nuxeo document property. */
    private static final String INVITATION_STATE_PROPERTY = "invitationState";
    /** Invitation local groups Nuxeo document property property. */
    private static final String INVITATION_LOCAL_GROUPS_PROPERTY = "invitationLocalGroups";


    /** Workspace identifier. */
    private final String workspaceId;
    /** Local group identifier. */
    private final String localGroupId;

    /** Log. */
    private final Log log;


    /**
     * Constructor.
     * 
     * @param workspaceId workspace identifier
     * @param localGroupId local group identifier
     */
    public UpdateRemovedLocalGroupLinkedInvitationsCommand(String workspaceId, String localGroupId) {
        super();
        this.workspaceId = workspaceId;
        this.localGroupId = localGroupId;

        this.log = LogFactory.getLog(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        // Invitations
        Documents invitations = this.getLinkedInvitations(nuxeoSession);

        for (Document invitation : invitations) {
            updateInvitation(documentService, invitation);
        }

        return invitations;
    }


    /**
     * Get linked invitations.
     * 
     * @param nuxeoSession Nuxeo session
     * @return invitations
     * @throws Exception
     */
    private Documents getLinkedInvitations(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:procedureModelWebId = '").append(IFormsService.FORMS_WEB_ID_PREFIX).append(MODEL_ID).append("' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*1/name = '").append(WORKSPACE_IDENTIFIER_PROPERTY).append("' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*1/value = '").append(this.workspaceId).append("' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*2/name = '").append(INVITATION_STATE_PROPERTY).append("' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*2/value = 'SENT' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*3/name = '").append(INVITATION_LOCAL_GROUPS_PROPERTY).append("' ");
        clause.append("AND ").append(GLOBAL_VARIABLES_PROPERTY).append("/*3/value LIKE '%").append(this.localGroupId).append("%' ");

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.Query");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        return (Documents) request.execute();
    }


    /**
     * Update invitation.
     * 
     * @param documentService document service
     * @param invitation invitation
     */
    private void updateInvitation(DocumentService documentService, Document invitation) {
        // Global variables
        PropertyList variables = invitation.getProperties().getList(GLOBAL_VARIABLES_PROPERTY);
        // Local groups variable
        Integer index = null;
        
        int i = 0;
        while ((i < variables.size()) && (index == null)) {
            PropertyMap map = variables.getMap(i);

            if (INVITATION_LOCAL_GROUPS_PROPERTY.equals(map.getString("name"))) {
                index = i;
            }

            i++;
        }
        
        if (index != null) {
            // Local groups identifiers
            Set<String> localGroupIds = new HashSet<>(Arrays.asList(StringUtils.split(variables.getMap(index).getString("value"), "|")));
            // Removed local group indicator
            boolean removed = localGroupIds.remove(this.localGroupId);

            if (removed) {
                // XPath
                StringBuilder xpath = new StringBuilder();
                xpath.append(GLOBAL_VARIABLES_PROPERTY);
                xpath.append("/");
                xpath.append(index);
                xpath.append("/value");

                String value = StringUtils.join(localGroupIds, "|");

                try {
                    documentService.setProperty(invitation, xpath.toString(), value);
                } catch (Exception e) {
                    this.log.error("Unable to update removed local group linked invitation.", e);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("|");
        builder.append(this.workspaceId);
        builder.append("|");
        builder.append(this.localGroupId);
        return builder.toString();
    }

}
