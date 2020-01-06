package org.osivia.services.widgets.move.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Move document Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MoveDocumentCommand implements INuxeoCommand {

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
    public MoveDocumentCommand(List<String> sourceIds, String targetId) {
        super();
        this.sourceIds = new HashSet<>(sourceIds);
        this.targetId = targetId;
    }


    @Override
    public Documents execute(Session nuxeoSession) throws Exception {
        // Target parent document reference
        DocRef target = new IdRef(this.targetId);

        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Moved documents
        Documents documents = new Documents(this.sourceIds.size());

        for (String sourceId : this.sourceIds) {
            // Source document reference
            DocRef source = new IdRef(sourceId);

            Document document = documentService.move(source, target);
            documents.add(document);
        }

        return documents;
    }


    @Override
    public String getId() {
        return null;
    }

}
