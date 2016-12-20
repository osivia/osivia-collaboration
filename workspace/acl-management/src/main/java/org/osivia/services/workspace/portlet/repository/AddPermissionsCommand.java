package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentSecurityService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Add permissions Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class AddPermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;
    /** Permissions. */
    private final List<Permission> permissions;
    /** Group of permissions where permissions are added. */
    private final String groupPermissions;
    /** Blocking inheritance permissions identifier. */
    private final boolean blockingInheritance;


    /**
     * Constructor.
     *
     * @param document document
     * @param permissions permissions
     */
    public AddPermissionsCommand(Document document, List<Permission> permissions, String groupPermissions,
            boolean blockingInheritance) {
        super();
        this.document = document;
        this.permissions = permissions;
        this.groupPermissions = groupPermissions;
        this.blockingInheritance = blockingInheritance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document execute(Session nuxeoSession) throws Exception {
        // Document Service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);
        // Permissions
        DocumentPermissions documentPermissions = PermissionsAdapter.getAs(this.permissions);
        
        return securityService.addPermissions(this.document, documentPermissions, this.groupPermissions, 
                this.blockingInheritance);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
