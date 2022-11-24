package org.osivia.services.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Create document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateDocumentCommand extends AbstractDocumentCommand {

    /**
     * Parent document path.
     */
    private String parentPath;
    /**
     * Document type.
     */
    private String type;


    /**
     * Constructor.
     */
    public CreateDocumentCommand() {
        super();
    }


    @Override
    public Object execute(Session session) throws Exception {
        // Document service
        DocumentService documentService = session.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef parent = new PathRef(this.parentPath);

        // Document creation
        DocRef document = documentService.createDocument(parent, this.type, null, this.getProperties());

        // Binaries
        this.updateBinaries(session, documentService, document);

        return document;
    }


    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setType(String type) {
        this.type = type;
    }
}
