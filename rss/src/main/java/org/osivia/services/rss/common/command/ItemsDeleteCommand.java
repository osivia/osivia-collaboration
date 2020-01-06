package org.osivia.services.rss.common.command;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Items Delete Nuxeo command.
 * Delete Items with the syncId 
 *
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemsDeleteCommand implements INuxeoCommand {

    private final List<String> items;

    /**
     * Constructor.
     *
     * @param items 
     */
    public ItemsDeleteCommand(List<String> items) {
        super();
        this.items = items;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {

    	for(String item : items) {
            DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
            documentService.remove(item);
    	}

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
