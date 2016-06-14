package org.osivia.services.workspace.portlet.repository.impl;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Remove permissions command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class RemovePermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;
    /** Names. */
    private final List<String> names;


    /**
     * Constructor.
     *
     * @param document document
     * @param names names
     */
    public RemovePermissionsCommand(Document document, List<String> names) {
        super();
        this.document = document;
        this.names = names;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        for (String name : this.names) {
            documentService.removePermissions(this.document, name, null);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
