package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentSecurityService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Remove permissions command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class RemovePermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;
    /** Permissions. */
    private final List<Permission> permissions;
    /** Users whose all permissions will be removed. */
    private List<String> userNames;
    /** Group of permissions where permissions are added. */
    private final String groupPermissions;
    /** Remove all permissions indicator. */
    private final boolean removeAll;
    /** Blocking inheritance permissions identifier. */
    private final boolean blockingInheritance;


    /**
     * Constructor.
     * 
     * @param document
     * @param permissions
     * @param groupPermissions
     * @param blockingInheritance
     */
    public RemovePermissionsCommand(Document document, List<Permission> permissions, String groupPermissions, boolean blockingInheritance) {
        super();
        this.document = document;
        this.permissions = permissions;
        this.userNames = null;
        this.groupPermissions = groupPermissions;
        this.removeAll = false;
        this.blockingInheritance = blockingInheritance;
    }
    
    /**
     * Constructor.
     * 
     * @param document
     * @param groupPermissions
     * @param blockingInheritance
     */
    public RemovePermissionsCommand(Document document, String groupPermissions, boolean blockingInheritance) {
        super();
        this.document = document;
        this.permissions = new ArrayList<Permission>(0);
        this.userNames = null;
        this.groupPermissions = groupPermissions;
        this.removeAll = true;
        this.blockingInheritance = blockingInheritance;
    }
    
    /**
     * Constructor.
     * 
     * @param document
     * @param userNames
     * @param groupPermissions
     * @param blockingInheritance
     */
    public RemovePermissionsCommand(Document document, String groupPermissions, List<String> userNames, boolean blockingInheritance) {
        super();
        this.document = document;
        this.permissions = new ArrayList<Permission>(0);
        this.userNames = userNames;
        this.groupPermissions = groupPermissions;
        this.removeAll = false;
        this.blockingInheritance = blockingInheritance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
     // Document Service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);
        // Permissions
        DocumentPermissions documentPermissions = PermissionsAdapter.getAs(this.permissions);
        
        return securityService.removePermissions(this.document, documentPermissions, this.userNames, this.groupPermissions, this.removeAll, 
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
