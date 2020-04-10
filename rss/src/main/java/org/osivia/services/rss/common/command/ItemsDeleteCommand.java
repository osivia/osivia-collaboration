package org.osivia.services.rss.common.command;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
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

    private final List<ItemRssModel> items;
    
	/** logger */
    protected static final Log logger = LogFactory.getLog(ItemsDeleteCommand.class);    

    /**
     * Constructor.
     *
     * @param items 
     */
    public ItemsDeleteCommand(List<ItemRssModel> items) {
        super();
        this.items = items;
    }


    @Override
    public Object execute(Session nuxeoSession) {
    	for(ItemRssModel item : this.items) {
            DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
            try {
				documentService.remove(item.getDocid());
			} catch (Exception e) {
				logger.error("Erreur lors de la synchronisation pour le flux:" + item.getGuid() + ';' + e.getCause().getMessage());
			}
    	}

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
