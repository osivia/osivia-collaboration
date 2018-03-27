package org.osivia.services.calendar.view.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Nuxeo get event command.
 *
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class EventGetCommand implements INuxeoCommand {

    /** Context path. */
    private final String contextPath;
    /** Document id */
    private final String docid;


    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     * @param startDate start date
     * @param endDate end date
     */
    public EventGetCommand(String contextPath, String docid) {
        super();
        this.contextPath = contextPath;
        this.docid = docid;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document
        DocRef docRef = new DocRef(this.docid);
        return documentService.getDocument(docRef,"*");
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Calendar/" + this.contextPath;
    }

}
