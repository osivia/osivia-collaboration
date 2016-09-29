package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Workspace permissions command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkspacePermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;
    /** Permissions. */
    private final List<Permission> permissions;


    /**
     * Constructor.
     *
     * @param document document
     * @param permissions permissions
     */
    public WorkspacePermissionsCommand(Document document, List<Permission> permissions) {
        super();
        this.document = document;
        this.permissions = permissions;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Add permissions
        for (Permission permission : this.permissions) {
            for (String value : permission.getValues()) {
                if (permission.isGranted()) {
                    documentService.addPermission(this.document, permission.getName(), value);
                } else {
                    documentService.setPermission(this.document, permission.getName(), value, false);
                }
            }
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
