package org.osivia.services.forum.thread.portlet.repository.command;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.forum.util.model.ForumFiles;
import org.osivia.services.forum.util.repository.command.AbstractForumCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Create forum thread post Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateForumThreadPostCommand extends AbstractForumCommand {

    /** Operation request identifier. */
    private static final String OPERATION_REQUEST_ID = "Document.AddComment";


    /** Forum thread document. */
    private final Document document;
    /** Forum thread post author. */
    private final String author;
    /** Forum thread post message. */
    private final String message;
    /** Forum thread post attachments . */
    private final ForumFiles attachments;


    /**
     * Constructor.
     *
     * @param document    forum thread document
     * @param author      forum thread post author
     * @param message     forum thread post message
     * @param attachments forum thread post attachments
     */
    public CreateForumThreadPostCommand(Document document, String author, String message, ForumFiles attachments) {
        super();
        this.document = document;
        this.author = author;
        this.message = message;
        this.attachments = attachments;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_REQUEST_ID);
        request.set("document", this.document);
        request.set("author", this.author);
        request.set("content", this.message);

        // Blobs
        if (CollectionUtils.isNotEmpty(this.attachments.getFiles())) {
            List<Blob> blobs = this.getBlobs(this.attachments.getFiles());
            if (!blobs.isEmpty()) {
                request.setInput(new Blobs(blobs));
            }
        }

        return request.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
