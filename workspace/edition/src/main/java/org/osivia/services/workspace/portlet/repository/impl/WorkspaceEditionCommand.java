package org.osivia.services.workspace.portlet.repository.impl;

import java.util.List;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.workspace.portlet.model.Task;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Workspace edition Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class WorkspaceEditionCommand implements INuxeoCommand {

    /** Form. */
    private final WorkspaceEditionForm form;


    /**
     * Constructor.
     *
     * @param form form
     */
    public WorkspaceEditionCommand(WorkspaceEditionForm form) {
        super();
        this.form = form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Update workspace
        this.updateWorkspace(documentService);

        // Update tasks
        this.updateTasks(nuxeoSession, documentService);

        return null;
    }


    /**
     * Update workspace.
     *
     * @param documentService document service
     * @throws Exception
     */
    private void updateWorkspace(DocumentService documentService) throws Exception {
        // Workspace reference
        DocRef workspace = new DocRef(this.form.getPath());

        // Edited properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", this.form.getTitle());
        properties.set("dc:description", this.form.getDescription());

        documentService.update(workspace, properties);
    }


    /**
     * Update tasks.
     *
     * @param nuxeoSession Nuxeo session
     * @param documentService document service
     * @throws Exception
     */
    private void updateTasks(Session nuxeoSession, DocumentService documentService) throws Exception {
        List<Task> tasks = this.form.getTasks();

        for (int i = (tasks.size() - 1); i >= 0; i--) {
            // Source
            Task sourceTask = tasks.get(i);
            DocRef source = new DocRef(sourceTask.getPath());

            // Update source
            PropertyMap properties = new PropertyMap();
            properties.set("ttc:showInMenu", sourceTask.isActive());
            documentService.update(source, properties);


            // Target
            DocRef target;
            if (i == (tasks.size() - 1)) {
                target = null;
            } else {
                Task targetTask = tasks.get(i + 1);
                target = new DocRef(targetTask.getPath());
            }

            // Reorder
            OperationRequest request = nuxeoSession.newRequest("Document.OrderDocument");
            request.set("sourceId", source);
            if (target != null) {
                request.set("targetId", target);
            }
            request.execute();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
