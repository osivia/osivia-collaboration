package org.osivia.services.widgets.rename.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.rename.portlet.repository.command.RenameCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Rename portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see RenameRepository
 */
@Repository
public class RenameRepositoryImpl implements RenameRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public RenameRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void rename(PortalControllerContext portalControllerContext, String path, String title) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(RenameCommand.class, path, title);
        nuxeoController.executeNuxeoCommand(command);
    }

}
