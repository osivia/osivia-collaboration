package org.osivia.services.workspace.portlet.repository;

import java.util.List;
import java.util.Map.Entry;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Update invitation requests Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateInvitationRequestsCommand implements INuxeoCommand {

    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Portal controller context. */
    private final PortalControllerContext portalControllerContext;
    /** Invitation requests. */
    private final List<InvitationRequest> invitationRequests;


    /**
     * Constructor.
     * 
     * @param portalControllerContext portal controller context
     * @param invitationRequests invitation requests
     */
    public UpdateInvitationRequestsCommand(PortalControllerContext portalControllerContext, List<InvitationRequest> invitationRequests) {
        super();
        this.portalControllerContext = portalControllerContext;
        this.invitationRequests = invitationRequests;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        for (InvitationRequest invitationRequest : this.invitationRequests) {
            // Document
            Document document = invitationRequest.getDocument();

            if (invitationRequest.isEdited()) {
                // Variables
                PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");
                variables.set(MemberManagementRepository.ROLE_PROPERTY, invitationRequest.getRole().getId());

                // Properties
                PropertyMap properties = new PropertyMap();
                properties.set("pi:globalVariablesValues", this.generateVariablesJSON(variables));

                documentService.update(document, properties);
            } else if (invitationRequest.isDeleted()) {
                this.proceed(nuxeoSession, invitationRequest, "actionIdNo");
            } else if (invitationRequest.isAccepted()) {
                this.proceed(nuxeoSession, invitationRequest, "actionIdYes");
            }
        }

        return null;
    }


    /**
     * Generate variables JSON content.
     *
     * @param variables variables
     * @return JSON
     */
    private String generateVariablesJSON(PropertyMap variables) {
        JSONArray array = new JSONArray();
        for (Entry<String, Object> entry : variables.getMap().entrySet()) {
            JSONObject object = new JSONObject();
            object.put("name", entry.getKey());
            object.put("value", entry.getValue());
            array.add(object);
        }
        return array.toString();
    }


    /**
     * Proceed task.
     * 
     * @param nuxeoSession Nuxeo session
     * @param invitationRequest invitation request
     * @param action task action name
     * @throws Exception
     */
    private void proceed(Session nuxeoSession, InvitationRequest invitationRequest, String action)
            throws Exception {
        // Task document
        Document task = this.getTask(nuxeoSession, invitationRequest);

        // Task variables
        PropertyMap taskVariables = task.getProperties().getMap("nt:task_variables");
        // Action identifier
        String actionId = taskVariables.getString(action);

        this.formsService.proceed(portalControllerContext, task, actionId, null);
    }


    /**
     * Get task document.
     * 
     * @param nuxeoSession Nuxeo session
     * @param invitationRequest invitation request
     * @return document
     * @throws Exception
     */
    private Document getTask(Session nuxeoSession, InvitationRequest invitationRequest) throws Exception {
        // Invitation request document
        Document requestDocument = invitationRequest.getDocument();

        // Task properties
        PropertyMap properties = requestDocument.getProperties().getMap("pi:task");
        // Task path
        String path = properties.getString("ecm:path");

        // Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM Document ");
        query.append("WHERE ecm:primaryType = 'TaskDoc' ");
        query.append("AND ecm:currentLifeCycleState = 'opened' ");
        query.append("AND ecm:path = '").append(path).append("' ");

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, task");
        request.set("query", query.toString());

        // Documents request result
        Documents documents = (Documents) request.execute();

        // Task document
        Document task;
        if (documents.size() == 1) {
            task = documents.get(0);
        } else {
            task = null;
        }

        return task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
