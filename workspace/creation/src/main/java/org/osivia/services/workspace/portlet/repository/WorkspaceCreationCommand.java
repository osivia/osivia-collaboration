package org.osivia.services.workspace.portlet.repository;

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
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Workspace creation Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspaceCreationCommand implements INuxeoCommand {

    /** Workspace creation form. */
    private final WorkspaceCreationForm form;
    /** Workspace parent path. */
    private final String parentPath;
    /** Workspace default taskbar items. */
    private final SortedSet<TaskbarItem> items;
    /** Internationalization bundle. */
    private final Bundle bundle;


    /**
     * Constructor.
     *
     * @param form workspace creation form
     * @param parentPath workspace parent path
     * @param items workspace default taskbar items
     * @param bundle internationalization bundle
     */
    public WorkspaceCreationCommand(WorkspaceCreationForm form, String parentPath, SortedSet<TaskbarItem> items, Bundle bundle) {
        super();
        this.form = form;
        this.parentPath = parentPath;
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
        Document workspace = this.createWorkspace(nuxeoSession, documentService);

        // Taskbar items
        this.createTaskbarItems(documentService, workspace);

        return workspace;
    }


    /**
     * Create workspace document.
     *
     * @param nuxeoSession Nuxeo session
     * @param documentService document service
     * @return document
     * @throws Exception
     */
    private Document createWorkspace(Session nuxeoSession, DocumentService documentService) throws Exception {
        // Workspaces container
        DocRef container = new DocRef(this.parentPath);

        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", this.form.getTitle());
        properties.set("dc:description", this.form.getDescription());
        properties.set("ttcs:visibility", this.form.getType().getId());

        // Name
        String name = this.generateNameFromTitle(this.form.getTitle());

        // Workspace creation
        Document workspace = documentService.createDocument(container, "Workspace", name, properties);

        // Update document
        OperationRequest request = nuxeoSession.newRequest("Document.FetchLiveDocument");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("value", workspace.getPath());
        return (Document) request.execute();
    }


    /**
     * Create taskbar items.
     *
     * @param documentService document service
     * @param workspace workspace document
     * @throws Exception
     */
    private void createTaskbarItems(DocumentService documentService, Document workspace) throws Exception {
        // Workspace shortname
        String shortname = workspace.getString("webc:url");
        // WebId prefix
        String webIdPrefix = ITaskbarService.WEBID_PREFIX + shortname + "_";

        for (TaskbarItem item : this.items) {
            String type;
            if (TaskbarItemType.CMS.equals(item.getType())) {
                type = StringUtils.defaultIfEmpty(item.getDocumentType(), "Staple");
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

            documentService.createDocument(workspace, type, name, properties);
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
