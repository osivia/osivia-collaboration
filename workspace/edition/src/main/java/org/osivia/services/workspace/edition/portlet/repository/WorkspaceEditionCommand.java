package org.osivia.services.workspace.edition.portlet.repository;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.services.workspace.edition.portlet.model.Image;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceEditionCommand implements INuxeoCommand {

    /** Options. */
    private final WorkspaceEditionOptions options;
    /** Form. */
    private final WorkspaceEditionForm form;


    /**
     * Constructor.
     *
     * @param options options
     * @param form form
     */
    public WorkspaceEditionCommand(WorkspaceEditionOptions options, WorkspaceEditionForm form) {
        super();
        this.options = options;
        this.form = form;
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
        // Workspace type
        WorkspaceType type = this.form.getType();

        documentService.setProperty(workspace, "dc:title", this.form.getTitle());
        documentService.setProperty(workspace, "dc:description", this.form.getDescription());
        if (this.form.isRoot()) {
            documentService.setProperty(workspace, "ttcs:visibility", type.getId());
        }

        if (WorkspaceType.PUBLIC.equals(type)) {
            // Grant read permission to everyone
            documentService.addPermission(workspace, "Everyone", "Read");
        } else {
            // TODO pouvoir supprimer une permission unitairement

            // Remove all permissions to everyone, including inheritance blocking
            documentService.removePermissions(workspace, "Everyone", null);

            // Maintain inheritance blocking
            documentService.setPermission(workspace, "Everyone", "Everything", false);
        }
        
        // Vignette
        Image vignette = this.form.getVignette();
        if (vignette.isUpdated()) {
            // Temporary file
            File temporaryFile = vignette.getTemporaryFile();
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(workspace, blob, "ttc:vignette");

            // Delete temporary file
            temporaryFile.delete();
        } else if (vignette.isDeleted()) {
            documentService.removeBlob(workspace, "ttc:vignette");
        }

        // Banner
        Image banner = this.form.getBanner();
        if (banner.isUpdated()) {
            // Temporary file
            File temporaryFile = banner.getTemporaryFile();
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(workspace, blob, "ttcs:headImage");

            // Delete temporary file
            temporaryFile.delete();
        } else if (banner.isDeleted()) {
            documentService.removeBlob(workspace, "ttcs:headImage");
        }
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

        // WebId prefix
        String webIdPrefix = ITaskbarService.WEBID_PREFIX + workspace.getString("webc:url") + "_";

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
                    webId = webIdPrefix + StringUtils.lowerCase(task.getId());
                }
                // Properties
                PropertyMap properties = new PropertyMap();
                properties.set("dc:title", title);
                if (StringUtils.isNotBlank(task.getDescription())) {
                    properties.set("dc:description", task.getDescription());
                }
                properties.set("ttc:showInMenu", true);
                if (webId != null) {
                    properties.set("ttc:webid", webId);
                }

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
