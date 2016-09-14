package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Set procedure instance ACL.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SetProcedureInstanceAcl implements INuxeoCommand {

    /** Workspace identifier. */
    private final String workspaceId;
    /** Person UID. */
    private final String uid;
    /** Workspace groups. */
    private final List<CollabProfile> groups;


    /**
     * Constructor.
     * 
     * @param modelPath model path
     * @param workspaceId workspace identifier
     * @param uid person UID
     * @param workspace groups
     */
    public SetProcedureInstanceAcl(String workspaceId, String uid, List<CollabProfile> groups) {
        super();
        this.workspaceId = workspaceId;
        this.uid = uid;
        this.groups = groups;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Procedure instance document
        Document instance = getProcedureInstance(nuxeoSession);

        for (CollabProfile group : groups) {
            documentService.addPermission(instance, group.getCn(), "Everything");
        }

        return null;
    }


    /**
     * Get procedure instance document.
     * 
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document getProcedureInstance(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:procedureModelWebId = '").append(IFormsService.FORMS_WEB_ID_PREFIX).append(MemberManagementRepository.MODEL_ID).append("' ");
        clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY).append(" = '").append(this.workspaceId)
                .append("' ");
        clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.INVITATION_STATE_PROPERTY).append(" = '")
                .append(InvitationState.SENT.toString()).append("' ");
        if (StringUtils.isNotEmpty(this.uid)) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.PERSON_UID_PROPERTY).append(" = '").append(this.uid).append("' ");
        }

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        Documents documents = (Documents) request.execute();
        return documents.get(0);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
