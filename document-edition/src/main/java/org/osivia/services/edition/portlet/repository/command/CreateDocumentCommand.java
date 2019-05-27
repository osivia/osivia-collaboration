package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Create document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateDocumentCommand implements INuxeoCommand {

    /**
     * Parent document path.
     */
    private final String parentPath;

    /**
     * Document type.
     */
    private final String type;

    /**
     * Document roperties.
     */
    private final PropertyMap properties;


    /**
     * Constructor.
     *
     * @param parentPath parent document path
     * @param type       document type
     * @param properties document properties
     */
    public CreateDocumentCommand(String parentPath, String type, PropertyMap properties) {
        super();
        this.parentPath = parentPath;
        this.type = type;
        this.properties = properties;
    }


    @Override
    public Object execute(Session session) throws Exception {
        // Document service
        DocumentService documentService = session.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef parent = new PathRef(this.parentPath);

        return documentService.createDocument(parent, this.type, null, this.properties);
    }


    @Override
    public String getId() {
        return null;
    }

}
