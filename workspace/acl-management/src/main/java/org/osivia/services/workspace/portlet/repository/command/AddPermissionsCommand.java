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
 * Add permissions Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractUpdatePermissionsCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddPermissionsCommand extends AbstractUpdatePermissionsCommand {

    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     * @param permissions
     * @param blockingInheritance
     */
    public AddPermissionsCommand(Document document, List<Permission> permissions, boolean blockingInheritance) {
        super(document, permissions, blockingInheritance);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document execute(Session nuxeoSession) throws Exception {
        // Document Service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);

        return securityService.addPermissions(this.getDocument(), this.getDocumentPermissions(), DocumentSecurityService.LOCAL_ACL,
                !this.isInherited());
    }

}
