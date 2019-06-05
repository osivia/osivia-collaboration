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
    private final String path;


    /**
     * Constructor.
     *
     * @param path       document path
     * @param properties document properties
     * @param binaries   document binaries
     */
    public UpdateDocumentCommand(String path, PropertyMap properties, Map<String, Blob> binaries) {
        super(properties, binaries);
        this.path = path;
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

}
