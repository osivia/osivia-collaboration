package org.osivia.services.workspace.sharing.portlet.repository.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update sharing permissions Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateSharingPermissionsCommand implements INuxeoCommand {

    /** Operation identifier. */
    private static final String OPERATION_ID = "Document.UpdateSharingPermissions";


    /** Document path. */
    private final String path;
    /** Sharing permission. */
    private final String permission;
    /** User. */
    private final String user;
    /** Add or remove permissions indicator. */
    private final Boolean add;


    /**
     * Constructor.
     * 
     * @param path document path
     * @param permission sharing permission
     * @param user user
     * @param add add or remove permissions indicator
     */
    public UpdateSharingPermissionsCommand(String path, String permission, String user, Boolean add) {
        super();
        this.path = path;
        this.permission = permission;
        this.user = user;
        this.add = add;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document reference
        DocRef ref = new PathRef(this.path);

        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_ID);
        request.setHeader(SharingRepository.ES_SYNC_FLAG, String.valueOf(true));
        request.setInput(ref);
        if (StringUtils.isNotEmpty(this.permission)) {
            request.set("permission", this.permission);
        }
        if (StringUtils.isNotEmpty(this.user)) {
            request.set("user", this.user);
        }
        if (this.add != null) {
            request.set("add", this.add);
        }

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
