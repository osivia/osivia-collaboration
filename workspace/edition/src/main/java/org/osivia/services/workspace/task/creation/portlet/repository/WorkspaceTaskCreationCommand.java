package org.osivia.services.workspace.task.creation.portlet.repository;

import java.text.Normalizer;
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
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.services.workspace.task.creation.portlet.model.TaskCreationForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Workspace task creation Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceTaskCreationCommand implements INuxeoCommand {

    /** Workspace path. */
    private final String workspacePath;
    /** Task creation form. */
    private final TaskCreationForm form;
    /** Default taskbar items. */
    private final SortedSet<TaskbarItem> items;
    /** Internationalization bundle. */
    private final Bundle bundle;


    /**
     * Constructor.
     * 
     * @param workspacePath workspace path
     * @param form task creation form
     * @param items default taskbar items
     * @param bundle internationalization bundle
     */
    public WorkspaceTaskCreationCommand(String workspacePath, TaskCreationForm form, SortedSet<TaskbarItem> items, Bundle bundle) {
        super();
        this.workspacePath = workspacePath;
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

        // Workspace reference
        DocRef workspace = new DocRef(this.workspacePath);

        // Type
        String type = this.form.getType();
        // Title
        String title = this.form.getTitle();
        // Description
        String description = this.form.getDescription();
        // Name
        String name = this.generateNameFromTitle(title);
        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", title);
        if (StringUtils.isNotBlank(description)) {
            properties.set("dc:description", description);
        }
        properties.set("ttc:showInMenu", true);

        // Creation
        Document document = documentService.createDocument(workspace, type, name, properties);

        if ("Room".equals(type)) {
            // Update document
            OperationRequest request = nuxeoSession.newRequest("Document.FetchLiveDocument");
            request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
            request.set("value", document.getPath());
            document = (Document) request.execute();

            // Taskbar items
            this.createTaskbarItems(documentService, document);
        }

        return document;
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
     * Create taskbar items.
     *
     * @param documentService document service
     * @param room room document
     * @throws Exception
     */
    private void createTaskbarItems(DocumentService documentService, Document room) throws Exception {
        // Workspace shortname
        String shortname = room.getString("webc:url");
        // WebId prefix
        String webIdPrefix = ITaskbarService.WEBID_PREFIX + shortname + "_";

        for (TaskbarItem item : this.items) {
            String type;
            if (TaskbarItemType.CMS.equals(item.getType())) {
                type = item.getDocumentType();
            } else {
                type = "Staple";
            }
            String title = this.bundle.getString(item.getKey(), item.getCustomizedClassLoader());
            String name = this.generateNameFromTitle(title);
            String webId = webIdPrefix + StringUtils.lowerCase(item.getId());

            // Properties
            PropertyMap properties = new PropertyMap();
            properties.set("dc:title", title);
            properties.set("ttc:showInMenu", true);
            properties.set("ttc:webid", webId);

            documentService.createDocument(room, type, name, properties);
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
