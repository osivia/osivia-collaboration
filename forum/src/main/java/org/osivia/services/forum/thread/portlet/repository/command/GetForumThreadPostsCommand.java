package org.osivia.services.forum.thread.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Get forum thread posts Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetForumThreadPostsCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String OPERATION_REQUEST_ID = "Fetch.DocumentComments";


    /** Forum thread document. */
    private final Document document;


    /**
     * Constructor.
     *
     * @param document forum thread document
     */
    public GetForumThreadPostsCommand(Document document) {
        super();
        this.document = document;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_REQUEST_ID);
        request.set("commentableDoc", this.document);

        return request.execute();
    }


    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("/");
        builder.append(this.document.getId());
        return builder.toString();
    }

}
