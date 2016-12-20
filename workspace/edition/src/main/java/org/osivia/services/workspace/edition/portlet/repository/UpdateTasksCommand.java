package org.osivia.services.workspace.edition.portlet.repository;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update workspace or room tasks Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateTasksCommand implements INuxeoCommand {

    /** Nuxeo document. */
    private final Document workspace;
    /** Tasks. */
    private final List<Task> tasks;


    /**
     * Constructor.
     *
     * @param workspace workspace or room Nuxeo document
     * @param tasks tasks
     */
    public UpdateTasksCommand(Document workspace, List<Task> tasks) {
        super();
        this.workspace = workspace;
        this.tasks = tasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // WebId prefix
        String webIdPrefix = ITaskbarService.WEBID_PREFIX + this.workspace.getString("webc:url") + "_";

        // Active tasks
        List<Task> activeTasks = new ArrayList<>(this.tasks.size());

        for (Task task : this.tasks) {
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


                // Created document
                Document document = documentService.createDocument(this.workspace, type, name, properties);

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

        return null;
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
