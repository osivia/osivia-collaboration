package org.osivia.services.taskbar.portlet.repository.command;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.services.taskbar.portlet.model.Task;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Create search staple Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateSearchStapleCommand implements INuxeoCommand {

    /** Base path. */
    private final String basePath;
    /** Taskbar task. */
    private final Task task;


    /**
     * Constructor.
     * 
     * @param basePath base path
     * @param task taskbar task
     */
    private CreateSearchStapleCommand(String basePath, Task task) {
        super();
        this.basePath = basePath;
        this.task = task;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Workspace
        Document workspace = documentService.getDocument(new DocRef(this.basePath), "*");
        // Workspace identifier
        String workspaceId = workspace.getString("webc:url");

        // WebId
        String webId = this.getWebId(workspaceId);

        // Task document
        Document document = this.getTaskDocument(nuxeoSession, webId);

        if (document == null) {
            // Properties
            PropertyMap properties = new PropertyMap();
            properties.set("dc:title", this.task.getDisplayName());
            properties.set("ttc:showInMenu", true);
            properties.set("ttc:webid", webId);
            properties.set("ttc:pageTemplate", StringUtils.trimToNull(this.task.getTemplate()));

            // Creation
            document = documentService.createDocument(workspace, "Staple", null, properties);
        } else if (BooleanUtils.isNotTrue(document.getProperties().getBoolean("ttc:showInMenu"))) {
            // Properties
            PropertyMap properties = new PropertyMap();
            properties.set("ttc:showInMenu", true);

            // Update
            document = documentService.update(document, properties);
        }

        return document;
    }


    /**
     * Get task webId.
     * 
     * @param workspaceId workspace identifier
     * @return webId
     */
    private String getWebId(String workspaceId) {
        StringBuilder webId = new StringBuilder();
        webId.append(ITaskbarService.WEBID_PREFIX);
        webId.append(workspaceId);
        webId.append("_");
        webId.append(StringUtils.lowerCase(this.task.getId()));
        return webId.toString();
    }


    /**
     * Get task document.
     * 
     * @param nuxeoSession Nuxeo session
     * @param webId task webId
     * @return document
     * @throws Exception
     */
    private Document getTaskDocument(Session nuxeoSession, String webId) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ttc:webid = '").append(webId).append("' ");

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        // Results
        Documents results = (Documents) request.execute();

        // Task document
        Document task;
        if (results.size() == 1) {
            task = results.iterator().next();
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
