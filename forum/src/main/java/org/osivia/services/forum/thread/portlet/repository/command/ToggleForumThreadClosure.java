package org.osivia.services.forum.thread.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.forum.thread.portlet.repository.ForumThreadRepository;
import org.osivia.services.forum.util.repository.command.AbstractForumCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Toggle forum thread closure Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ToggleForumThreadClosure implements INuxeoCommand {

    /** Forum thread Nuxeo document. */
    private final Document thread;
    /** Closure indicator. */
    private final boolean close;


    /**
     * Constructor.
     *
     * @param thread forum thread Nuxeo document
     * @param close  closure indicator
     */
    public ToggleForumThreadClosure(Document thread, boolean close) {
        super();
        this.thread = thread;
        this.close = close;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Update document
        Document updatedThread = documentService.setProperty(this.thread, ForumThreadRepository.CLOSED_PROPERTY, String.valueOf(this.close));

        return updatedThread;
    }


    @Override
    public String getId() {
        return null;
    }

}
