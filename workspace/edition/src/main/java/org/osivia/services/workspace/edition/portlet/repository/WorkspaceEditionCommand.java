package org.osivia.services.workspace.edition.portlet.repository;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Workspace edition Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class WorkspaceEditionCommand implements INuxeoCommand {

    /** Options. */
    private final WorkspaceEditionOptions options;
    /** Form. */
    private final WorkspaceEditionForm form;
    /** Default taskbar items. */
    private final SortedSet<TaskbarItem> items;
    /** Internationalization bundle. */
    private final Bundle bundle;


    /**
     * Constructor.
     *
     * @param options options
     * @param form form
     * @param items default taskbar items
     * @param bundle internationalization bundle
     */
    public WorkspaceEditionCommand(WorkspaceEditionOptions options, WorkspaceEditionForm form, SortedSet<TaskbarItem> items, Bundle bundle) {
        super();
        this.options = options;
        this.form = form;
        this.items = items;
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
        request.set("value", this.options.getPath());
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
        documentService.setProperty(workspace, "dc:title", this.form.getTitle());
        documentService.setProperty(workspace, "dc:description", this.form.getDescription());
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
                String title = task.getDisplayName();
                // Name
                String name = this.generateNameFromTitle(title);
                // WebId
                String webId;
                if (task.getId() == null) {
                    webId = null;
                } else {
                    webId = worskpaceId + "_" + StringUtils.lowerCase(task.getId());
                }
                // Properties
                PropertyMap properties = new PropertyMap();
                properties.set("dc:title", title);
                properties.set("ttc:showInMenu", true);
                if (webId != null) {
                    properties.set("ttc:webid", webId);
                }

                // Creation
                Document document = documentService.createDocument(workspace, type, name, properties);

                if (StringUtils.isNotBlank(task.getDescription())) {
                    documentService.setProperty(document, "dc:description", task.getDescription());
                }

                if ("Room".equals(type)) {
                    // Update document
                    OperationRequest request = nuxeoSession.newRequest("Document.FetchLiveDocument");
                    request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
                    request.set("value", document.getPath());
                    document = (Document) request.execute();

                    // Taskbar items
                    this.createTaskbarItems(documentService, document);
                }


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
     * Create taskbar items.
     *
     * @param documentService document service
     * @param room room document
     * @throws Exception
     */
    private void createTaskbarItems(DocumentService documentService, Document room) throws Exception {
        // Workspace identifier
        String identifier = room.getString("webc:url");

        for (TaskbarItem item : this.items) {
            String type = item.getDocumentType();
            String title = this.bundle.getString(item.getKey(), item.getCustomizedClassLoader());
            String name = this.generateNameFromTitle(title);
            String webId = identifier + "_" + StringUtils.lowerCase(item.getId());

            // Properties
            PropertyMap properties = new PropertyMap();
            properties.set("dc:title", title);
            properties.set("ttc:showInMenu", true);
            properties.set("ttc:webid", webId);

            documentService.createDocument(room, type, name, properties);
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
