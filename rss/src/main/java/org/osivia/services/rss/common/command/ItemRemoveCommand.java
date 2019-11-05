package org.osivia.services.rss.common.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Item remove Nuxeo command.
 *
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemRemoveCommand implements INuxeoCommand {


    private final String docid;


    /**
     * Constructor.
     *
     * @param docid    document id
     */
    public ItemRemoveCommand(String docid) {
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
