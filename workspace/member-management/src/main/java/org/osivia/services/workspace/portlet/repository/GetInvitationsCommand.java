package org.osivia.services.workspace.portlet.repository;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

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


    /**
     * Constructor.
     *
     * @param modelPath model path
     * @param workspaceId workspace identifier
     */
    public GetInvitationsCommand(String modelPath, String workspaceId) {
        super();
        this.modelPath = modelPath;
        this.workspaceId = workspaceId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'TaskDoc' ");
        clause.append("AND nt:pi.pi:procedureModelPath = \"").append(this.modelPath).append("\" ");
        clause.append("AND nt:pi.pi:globalVariablesValues.workspaceId = '").append(this.workspaceId).append("' ");
        clause.append("AND ecm:currentLifeCycleState != 'ended' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM Document WHERE " + clause);

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
        return builder.toString();
    }

}
