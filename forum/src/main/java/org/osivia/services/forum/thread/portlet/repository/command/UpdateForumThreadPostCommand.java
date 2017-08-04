package org.osivia.services.forum.thread.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.osivia.services.forum.util.model.ForumFiles;
import org.osivia.services.forum.util.repository.command.AbstractForumCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Update forum thread post Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateForumThreadPostCommand extends AbstractForumCommand {

    /** Forum thread post identifier. */
    private final String id;
    /** Forum thread post message. */
    private final String message;
    /** Forum thread post attachments. */
    private final ForumFiles attachments;


    /**
     * Constructor.
     *
     * @param id          forum thread post identifier
     * @param message     forum thread post message
     * @param attachments forum thread post attachments
     */
    public UpdateForumThreadPostCommand(String id, String message, ForumFiles attachments) {
        super();
        this.id = id;
        this.message = message;
        this.attachments = attachments;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document reference
        DocRef ref = new DocRef(this.id);
        documentService.setProperty(ref, "post:text", this.message);

        // Set blobs
        this.setBlobs(documentService, ref, this.attachments);

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
