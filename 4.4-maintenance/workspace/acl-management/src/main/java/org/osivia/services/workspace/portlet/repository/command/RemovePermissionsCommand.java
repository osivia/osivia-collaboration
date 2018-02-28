package org.osivia.services.workspace.portlet.repository.command;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentSecurityService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.workspace.portlet.model.Permission;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Remove permissions command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractUpdatePermissionsCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RemovePermissionsCommand extends AbstractUpdatePermissionsCommand {

    /** Users whose all permissions will be removed. */
    private List<String> users;
    /** Remove all permissions indicator. */
    private final boolean removeAll;


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     */
    public RemovePermissionsCommand(Document document) {
        this(document, null, false, null, true);
    }


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     * @param permissions permissions
     * @param inherited inherited indicator
     */
    public RemovePermissionsCommand(Document document, List<Permission> permissions, boolean inherited) {
        this(document, permissions, inherited, null, false);
    }


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     * @param permissions permissions
     * @param inherited inherited indicator
     * @param users user names
     */
    public RemovePermissionsCommand(Document document, List<Permission> permissions, boolean inherited, List<String> users) {
        this(document, permissions, inherited, users, false);
    }


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     * @param permissions permissions
     * @param inherited inherited indicator
     * @param users user names
     * @param removeAll remove all indicator
     */
    private RemovePermissionsCommand(Document document, List<Permission> permissions, boolean inherited, List<String> users, boolean removeAll) {
        super(document, permissions, inherited);
        this.users = users;
        this.removeAll = removeAll;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document Service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);
        
        return securityService.removePermissions(this.getDocument(), this.getDocumentPermissions(), this.users, DocumentSecurityService.LOCAL_ACL,
                this.removeAll, !this.isInherited());
    }

}
