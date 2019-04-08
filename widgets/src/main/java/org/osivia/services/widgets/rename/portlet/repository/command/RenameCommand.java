package org.osivia.services.widgets.rename.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Rename document Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenameCommand implements INuxeoCommand {

    /** Document path. */
    private final String path;
    /** Document title. */
    private final String title;


    /**
     * Constructor.
     * 
     * @param path document path
     * @param title document title
     */
    public RenameCommand(String path, String title) {
        super();
        this.path = path;
        this.title = title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document reference
        DocRef ref = new DocRef(this.path);
        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", this.title);

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
