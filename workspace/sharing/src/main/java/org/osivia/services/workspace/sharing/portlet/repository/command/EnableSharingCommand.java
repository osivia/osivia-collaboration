package org.osivia.services.workspace.sharing.portlet.repository.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
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
        // Enable sharing
        this.enableSharing(nuxeoSession);

        // Fill sharing link
        return this.fillLink(nuxeoSession);
    }


    /**
     * Enable sharing.
     * 
     * @param nuxeoSession Nuxeo session
     * @throws Exception
     */
    private void enableSharing(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_ID);

        // Document reference
        DocRef ref = new PathRef(this.path);
        request.setInput(ref);

        request.execute();
    }


    /**
     * Fill sharing link.
     * 
     * @param nuxeoSession Nuxeo session
     * @throws Exception
     */
    private Document fillLink(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document reference
        DocRef ref = new PathRef(this.path);

        // Updated properties
        PropertyMap properties = new PropertyMap();
        properties.set(SharingRepository.SHARING_LINK_ID_PROPERTY, this.link.getId());
        properties.set(SharingRepository.SHARING_LINK_PERMISSION_PROPERTY, this.link.getPermission().getId());

        return documentService.update(ref, properties, true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
