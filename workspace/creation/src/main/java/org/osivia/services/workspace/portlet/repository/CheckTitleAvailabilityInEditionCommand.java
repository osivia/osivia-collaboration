package org.osivia.services.workspace.portlet.repository;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Check workspace title availability Nuxeo command.
 * 
 * @author ckrommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckTitleAvailabilityInEditionCommand implements INuxeoCommand {

    /** Workspace creation model webId. */
    private final String modelWebId;
    /** Procedure instance UUID. */
    private final String procedureInstanceUuid;
    /** Workspace title. */
    private final String title;
    /** Workspace title variable name. */
    private final String titleVariableName;


    /**
     * Constructor.
     *
     * @param modelWebId workspace creation model webId
     * @param procedureInstanceUuid procedure instance UUID
     * @param title workspace title
     * @param titleVariableName workspace title variable name
     */
    public CheckTitleAvailabilityInEditionCommand(String modelWebId, String procedureInstanceUuid, String title, String titleVariableName) {
        super();
        this.modelWebId = modelWebId;
        this.procedureInstanceUuid = procedureInstanceUuid;
        this.title = title;
        this.titleVariableName = titleVariableName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        boolean available = this.checkInExistingWorkspaces(nuxeoSession);

        if (available) {
            available = this.checkInPendingWorkspaces(nuxeoSession);
        }
        
        return available;
    }


    /**
     * Check workspace title in existing workspaces.
     * 
     * @param nuxeoSession Nuxeo session
     * @return true if no workspace with this title exists
     * @throws Exception
     */
    private boolean checkInExistingWorkspaces(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'Workspace' ");
        clause.append("AND dc:title ILIKE '").append(StringUtils.replace(this.title, "'", "\\'")).append("' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        request.set("query", "SELECT * FROM Document WHERE " + clause.toString());

        // Results
        Documents results = (Documents) request.execute();

        return results.isEmpty();
    }


    /**
     * Check workspace title in pending workspaces.
     * 
     * @param nuxeoSession Nuxeo session
     * @return true if no pending workspace with this title exists
     * @throws Exception
     */
    private boolean checkInPendingWorkspaces(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
        clause.append("AND pi:procedureModelWebId = '").append(this.modelWebId).append("' ");
        if (StringUtils.isNotEmpty(this.procedureInstanceUuid)) {
            clause.append("AND pi:globalVariablesValues.uuid <> '").append(this.procedureInstanceUuid).append("' ");
        }
        clause.append("AND pi:task.ecm:currentLifeCycleState = 'opened' ");
        clause.append("AND pi:globalVariablesValues.").append(this.titleVariableName).append(" = '").append(StringUtils.replace(this.title, "'", "\\'"))
                .append("' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        request.set("query", "SELECT * FROM Document WHERE " + clause.toString());

        // Results
        Documents results = (Documents) request.execute();

        return results.isEmpty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
