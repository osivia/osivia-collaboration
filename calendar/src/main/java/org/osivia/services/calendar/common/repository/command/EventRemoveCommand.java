package org.osivia.services.calendar.common.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Agenda remove Nuxeo command.
 *
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventRemoveCommand implements INuxeoCommand {


    /** Calendar edition form. */
    private final String docid;


    /**
     * Constructor.
     *
     * @param docid    document id
     */
    public EventRemoveCommand(String docid) {
        super();
        this.docid = docid;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        documentService.remove(docid);

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
