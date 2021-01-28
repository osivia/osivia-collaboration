package org.osivia.services.workspace.sharing.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Update sharing permissions Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateSharingPermissionsCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    private static final String OPERATION_ID = "Document.UpdateSharingPermissions";


    /**
     * Document path.
     */
    private String path;
    /**
     * Sharing permission.
     */
    private String permission;
    /**
     * User.
     */
    private String user;
    /**
     * Add or remove permissions indicator.
     */
    private Boolean add;
    /**
     * Ban or unban user indicator.
     */
    private Boolean ban;


    /**
     * Constructor.
     */
    public UpdateSharingPermissionsCommand() {
        super();
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
        if (this.ban != null) {
            request.set("ban", this.ban);
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


    public void setPath(String path) {
        this.path = path;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAdd(Boolean add) {
        this.add = add;
    }

    public void setBan(Boolean ban) {
        this.ban = ban;
    }
}
