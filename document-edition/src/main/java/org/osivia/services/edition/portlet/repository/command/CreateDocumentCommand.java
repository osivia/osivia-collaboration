package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    private final String parentPath;
    /**
     * Document type.
     */
    private final String type;


    /**
     * Constructor.
     *
     * @param parentPath parent document path
     * @param type       document type
     * @param properties document properties
     * @param binaries   document binaries
     */
    public CreateDocumentCommand(String parentPath, String type, PropertyMap properties, Map<String, Blob> binaries) {
        super(properties, binaries);
        this.parentPath = parentPath;
        this.type = type;
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
        this.updateBinaries(documentService, document);

        return document;
    }

}
