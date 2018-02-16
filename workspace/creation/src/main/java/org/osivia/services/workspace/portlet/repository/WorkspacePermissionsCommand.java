package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentSecurityService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;
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
        // Document security service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);
        // Get permissions
        DocumentPermissions docPermissions = PermissionsAdapter.getAs(this.permissions);
        
        securityService.addPermissions(this.document, docPermissions, PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, true);
        
        // Remove creator of Workspace from ACLs (default Nx rule)
        String username = nuxeoSession.getLogin().getUsername();
        List<String> userNames = new ArrayList<>(1);
        userNames.add(username);
        
        return securityService.removePermissions(this.document, null, userNames, PermissionsAdapter.LOCAL_GROUP_PERMISSIONS, false, true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
