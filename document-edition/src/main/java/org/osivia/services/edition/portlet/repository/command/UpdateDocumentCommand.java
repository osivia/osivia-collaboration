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
 * Update document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateDocumentCommand implements INuxeoCommand {

    /**
     * Document path.
     */
    private final String path;

    /**
     * Properties.
     */
    private final PropertyMap properties;


    /**
     * Constructor.
     *
     * @param path       document path
     * @param properties document properties
     */
    public UpdateDocumentCommand(String path, PropertyMap properties) {
        super();
        this.path = path;
        this.properties = properties;
    }


    @Override
    public Object execute(Session session) throws Exception {
        // Document service
        DocumentService documentService = session.getAdapter(DocumentService.class);

        // Document reference
        DocRef document = new PathRef(this.path);

        return documentService.update(document, this.properties);
    }


    @Override
    public String getId() {
        return null;
    }

}
