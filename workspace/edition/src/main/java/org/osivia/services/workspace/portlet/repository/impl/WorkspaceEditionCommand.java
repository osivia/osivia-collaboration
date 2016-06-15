package org.osivia.services.workspace.portlet.repository.impl;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.taskbar.TaskbarItemType;
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
    /** Internationalization bundle. */
    private final Bundle bundle;


    /**
     * Constructor.
     *
     * @param form form
     * @param bundle internationalization bundle
     */
    public WorkspaceEditionCommand(WorkspaceEditionForm form, Bundle bundle) {
        super();
        this.form = form;
        this.bundle = bundle;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Workspace
        Document workspace = this.getWorkspace(nuxeoSession);

        // Update workspace
        this.updateWorkspace(documentService, workspace);

        // Update tasks
        this.updateTasks(nuxeoSession, documentService, workspace);

        return null;
    }


    /**
     * Get workspace document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document getWorkspace(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest("Document.FetchLiveDocument");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("value", this.form.getPath());
        return (Document) request.execute();
    }


    /**
     * Update workspace.
     *
     * @param documentService document service
     * @param workspace workspace document
     * @throws Exception
     */
    private void updateWorkspace(DocumentService documentService, Document workspace) throws Exception {
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
     * @param workspace workspace document
     * @throws Exception
     */
    private void updateTasks(Session nuxeoSession, DocumentService documentService, Document workspace) throws Exception {
        List<Task> tasks = this.form.getTasks();

        // Workspace identifier
        String worskpaceId = workspace.getString("webc:url");


        // Active tasks
        List<Task> activeTasks = new ArrayList<>(tasks.size());

        for (Task task : tasks) {
            if (task.isActive()) {
                activeTasks.add(task);
            }

            if (task.getPath() != null) {
                // Document reference
                DocRef ref = new DocRef(task.getPath());

                // Update document
                PropertyMap properties = new PropertyMap();
                properties.set("ttc:showInMenu", task.isActive());
                documentService.update(ref, properties);
            } else if (task.isActive()) {
                // Type
                String type;
                if (TaskbarItemType.CMS.equals(task.getType())) {
                    type = task.getDocumentType();
                } else {
                    type = "Staple";
                }
                // Title
                String title = this.bundle.getString(task.getKey(), task.getCustomizedClassLoader());
                // Name
                String name = this.generateNameFromTitle(title);
                // WebId
                String webId = worskpaceId + "_" + StringUtils.lowerCase(task.getId());
                // Properties
                PropertyMap properties = new PropertyMap();
                properties.set("dc:title", title);
                properties.set("ttc:showInMenu", true);
                properties.set("ttc:webid", webId);

                // Creation
                Document document = documentService.createDocument(workspace, type, name, properties);

                // Update task
                task.setPath(document.getPath());
                task.setActive(true);
            }
        }


        // Reorder active tasks
        for (int i = (activeTasks.size() - 1); i >= 0; i--) {
            // Source
            Task sourceTask = activeTasks.get(i);
            DocRef source = new DocRef(sourceTask.getPath());

            // Target
            DocRef target;
            if (i == (activeTasks.size() - 1)) {
                target = null;
            } else {
                Task targetTask = activeTasks.get(i + 1);
                target = new DocRef(targetTask.getPath());
            }

            // Operation request
            OperationRequest request = nuxeoSession.newRequest("Document.OrderDocument");
            request.set("sourceId", source);
            if (target != null) {
                request.set("targetId", target);
            }

            // Execution
            request.execute();
        }
    }


    /**
     * Generate document name from title.
     *
     * @param title document title
     * @return document name
     */
    private String generateNameFromTitle(String title) {
        String name = title;

        // Lower case
        name = StringUtils.lowerCase(name);

        // Remove accents
        name = Normalizer.normalize(name, Normalizer.Form.NFD);
        name = name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Remove special characters
        name = name.replaceAll("[^a-z0-9]", "-");

        // Remove "-" prefix
        while (StringUtils.startsWith(name, "-")) {
            name = StringUtils.removeStart(name, "-");
        }

        // Remove "-" suffix
        while (StringUtils.endsWith(name, "-")) {
            name = StringUtils.removeEnd(name, "-");
        }

        // Remove consecutive "-"
        while (StringUtils.contains(name, "--")) {
            name = StringUtils.replace(name, "--", "-");
        }

        return name;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
