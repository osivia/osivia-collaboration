package org.osivia.services.workspace.portlet.repository;

import java.util.List;
import java.util.Map.Entry;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Update invitations Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateInvitationsCommand implements INuxeoCommand {

    /** Invitations. */
    private final List<Invitation> invitations;


    /**
     * Constructor.
     * 
     * @param invitations invitations
     */
    public UpdateInvitationsCommand(List<Invitation> invitations) {
        super();
        this.invitations = invitations;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        for (Invitation invitation : invitations) {
            // Document
            Document document = invitation.getDocument();

            if (invitation.isDeleted()) {
                // TODO Pour David : changer "remove" en "cancel procedure". Have fun !
                documentService.remove(document);
            } else {
                // Variables
                PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");
                variables.set("role", invitation.getRole().getId());

                // Properties
                PropertyMap properties = new PropertyMap();
                properties.set("pi:globalVariablesValues", this.generateVariablesJSON(variables));

                documentService.update(document, properties);
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
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}