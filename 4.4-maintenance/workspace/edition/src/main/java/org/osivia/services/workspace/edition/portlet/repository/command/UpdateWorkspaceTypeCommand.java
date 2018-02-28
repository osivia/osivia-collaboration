package org.osivia.services.workspace.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Update workspace type command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateWorkspaceTypeCommand implements INuxeoCommand {

    /** Workspace Nuxeo document. */
    private final Document workspace;
    /** Workspace type. */
    private final WorkspaceType workspaceType;


    /**
     * Constructor.
     *
     * @param workspace workspace Nuxeo document
     * @param workspaceType workspace type
     */
    public UpdateWorkspaceTypeCommand(Document workspace, WorkspaceType workspaceType) {
        super();
        this.workspace = workspace;
        this.workspaceType = workspaceType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        documentService.setProperty(this.workspace, "ttcs:visibility", this.workspaceType.getId());

        if (WorkspaceType.PUBLIC.equals(this.workspaceType) || WorkspaceType.PUBLIC_INVITATION.equals(this.workspaceType)) {
            // Grant read permission to everyone
            documentService.addPermission(this.workspace, "Everyone", "Read");
        } else {
            // TODO pouvoir supprimer une permission unitairement

            // Remove all permissions to everyone, including inheritance blocking
            documentService.removePermissions(this.workspace, "Everyone", null);

            // Maintain inheritance blocking
            documentService.setPermission(this.workspace, "Everyone", "Everything", false);
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
