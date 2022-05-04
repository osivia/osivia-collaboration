package org.osivia.services.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Update document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateDocumentCommand extends AbstractDocumentCommand {

    /**
     * Document path.
     */
    private String path;


    /**
     * Constructor.
     */
    public UpdateDocumentCommand() {
        super();
    }


    @Override
    public Object execute(Session session) throws Exception {
        // Document service
        DocumentService documentService = session.getAdapter(DocumentService.class);

        // Document reference
        DocRef document = new PathRef(this.path);

        // Document update
        document = documentService.update(document, this.getProperties());

        // Binaries
        this.updateBinaries(documentService, document);

        return document;
    }


    public void setPath(String path) {
        this.path = path;
    }
}
