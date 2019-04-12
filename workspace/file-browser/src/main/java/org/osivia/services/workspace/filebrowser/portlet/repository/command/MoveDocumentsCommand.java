package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Move documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MoveDocumentsCommand implements INuxeoCommand {

    /** Source documents identifiers. */
    private final Set<String> sourceIds;
    /** Target parent document identifier. */
    private final String targetId;


    /**
     * Constructor.
     *
     * @param sourceIds source documents identifiers
     * @param targetId target parent document identifier
     */
    public MoveDocumentsCommand(List<String> sourceIds, String targetId) {
        super();
        this.sourceIds = new HashSet<String>(sourceIds);
        this.targetId = targetId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Target parent document reference
        DocRef target = new IdRef(this.targetId);

        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        for (String sourceId : this.sourceIds) {
            // Source document reference
            DocRef source = new IdRef(sourceId);

            documentService.move(source, target);
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
