package org.osivia.services.forum.thread.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Delete forum thread post Nuxeo command
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteForumThreadPostCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String OPERATION_REQUEST_ID = "Document.DeleteComment";


    /** Forum thread document. */
    private final Document document;
    /** Deleted forum thread post identifier. */
    private final String id;


    /**
     * Constructor.
     *
     * @param document forum thread document
     * @param id       deleted forum thread post identifier
     */
    public DeleteForumThreadPostCommand(Document document, String id) {
        this.document = document;
        this.id = id;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_REQUEST_ID);
        request.set("commentableDoc", this.document.getId());
        request.set("comment", this.id);

        return request.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
