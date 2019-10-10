package org.osivia.services.rss.integration.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.integration.RssNuxeoCommand;
import org.osivia.services.rss.integration.Model.RssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Rss creation Nuxeo command.
 * 
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 * @see AbstractCalendarCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RssRepositoryImpl {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
    

    /**
     * Constructor.
     */
    public RssRepositoryImpl() {
        super();
    }
	
    /**
     * {@inheritDoc}
     */
    public void save(PortalControllerContext portalControllerContext, RssModel model) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        command = this.applicationContext.getBean(RssNuxeoCommand.class, model);

        nuxeoController.executeNuxeoCommand(command);
    }
	
}
