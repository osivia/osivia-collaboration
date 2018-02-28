package org.osivia.services.workspace.edition.portlet.repository.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
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
public class CheckTitleAvailabilityCommand implements INuxeoCommand {

    /** Create workspace filter identifier. */
    private static final String CREATE_WORKSPACE_FILTER_ID = "CREATE_WORKSPACE";
    /** Create workspace filter title variable name. */
    private static final String CREATE_WORKSPACE_FILTER_TITLE_VARIABLE_NAME = "titleVariableName";


    /** Workspace path. */
    private final String path;
    /** Workspace title. */
    private final String title;


    /**
     * Constructor.
     *
     * @param title workspace title
     */
    public CheckTitleAvailabilityCommand(WorkspaceEditionForm form) {
        super();
        this.path = form.getDocument().getPath();
        this.title = form.getTitle();
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
        clause.append("AND ecm:path <> '").append(this.path).append("' ");
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
        // Model
        Document model = this.getModel(nuxeoSession);

        // Available workspace title indicator
        boolean available;

        if (model == null) {
            available = true;
        } else {
            // Model webId
            String modelWebId = model.getString("ttc:webid");

            // Workspace title variable name
            String titleVariableName = this.getTitleVariableName(model);


            // Clause
            StringBuilder clause = new StringBuilder();
            clause.append("ecm:primaryType = 'ProcedureInstance' ");
            clause.append("AND pi:procedureModelWebId = '").append(modelWebId).append("' ");
            clause.append("AND pi:currentStep != 'information' ");
            clause.append("AND pi:task.ecm:currentLifeCycleState = 'opened' ");
            clause.append("AND pi:globalVariablesValues.").append(titleVariableName).append(" = '").append(StringUtils.replace(this.title, "'", "\\'"))
                    .append("' ");

            // Operation request
            OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
            request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
            request.set("query", "SELECT * FROM Document WHERE " + clause.toString());

            // Results
            Documents results = (Documents) request.execute();

            available = results.isEmpty();
        }

        return available;
    }


    /**
     * Get workspace creation model.
     * 
     * @param nuxeoSession Nuxeo session
     * @return Nuxeo document
     * @throws Exception
     */
    private Document getModel(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureModel' ");
        clause.append("AND pcd:steps.actions.filtersList.filterId = '").append(CREATE_WORKSPACE_FILTER_ID).append("' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice, procedure");
        request.set("query", "SELECT * FROM Document WHERE " + clause.toString());

        // Results
        Documents results = (Documents) request.execute();

        // Model
        Document model;
        if (results.size() == 1) {
            model = results.get(0);
        } else {
            model = null;
        }

        return model;
    }


    /**
     * Get workspace title variable name.
     * 
     * @param model model Nuxeo document
     * @return workspace title variable name
     */
    private String getTitleVariableName(Document model) {
        // Workspace title variable name
        String titleVariableName = null;

        // Steps
        PropertyList steps = model.getProperties().getList("pcd:steps");

        for (int i = 0; i < steps.size(); i++) {
            // Step
            PropertyMap step = steps.getMap(i);

            // Step actions
            PropertyList actions = step.getList("actions");

            for (int j = 0; j < actions.size(); j++) {
                // Action
                PropertyMap action = actions.getMap(j);
                // Action filters
                PropertyList filters = action.getList("filtersList");

                for (int k = 0; k < filters.size(); k++) {
                    // Filter
                    PropertyMap filter = filters.getMap(k);
                    // Filter identifier
                    String filterId = filter.getString("filterId");

                    if (StringUtils.equals(CREATE_WORKSPACE_FILTER_ID, filterId)) {
                        // Filter arguments
                        PropertyList arguments = filter.getList("argumentsList");

                        for (int l = 0; l < arguments.size(); l++) {
                            // Argument
                            PropertyMap argument = arguments.getMap(l);
                            // Argument name
                            String argumentName = argument.getString("argumentName");

                            if (StringUtils.equals(CREATE_WORKSPACE_FILTER_TITLE_VARIABLE_NAME, argumentName)) {
                                titleVariableName = argument.getString("argumentValue");
                                break;
                            }
                        }
                    }

                    if (StringUtils.isNotEmpty(titleVariableName)) {
                        break;
                    }
                }

                if (StringUtils.isNotEmpty(titleVariableName)) {
                    break;
                }
            }

            if (StringUtils.isNotEmpty(titleVariableName)) {
                break;
            }
        }
        return titleVariableName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
