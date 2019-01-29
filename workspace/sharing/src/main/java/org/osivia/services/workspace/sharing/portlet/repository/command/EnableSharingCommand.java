package org.osivia.services.workspace.sharing.portlet.repository.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Enable sharing Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EnableSharingCommand implements INuxeoCommand {

    /** Operation identifier. */
    private static final String OPERATION_ID = "Document.EnableSharing";


    /** Document path. */
    private final String path;
    /** Sharing link. */
    private final SharingLink link;


    /**
     * Constructor.
     * 
     * @param path document path
     * @param link sharing link
     */
    public EnableSharingCommand(String path, SharingLink link) {
        super();
        this.path = path;
        this.link = link;
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
        request.set("linkId", this.link.getId());
        request.set("permission", this.link.getPermission().getId());

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
