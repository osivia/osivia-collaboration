package org.osivia.services.workspace.sharing.portlet.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.portlet.repository.command.DisableSharingCommand;
import org.osivia.services.workspace.sharing.portlet.repository.command.EnableSharingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Sharing portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingRepository
 */
@Repository
public class SharingRepositoryImpl implements SharingRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public SharingRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharingEnabled(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDocument();

        // Facets
        PropertyList facets = document.getFacets();

        boolean enabled = false;
        if (facets != null) {
            int i = 0;
            while (!enabled && (i < facets.size())) {
                String facet = facets.getString(i);
                enabled = SHARING_FACET.equals(facet);
                i++;
            }
        }

        return enabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void enableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(EnableSharingCommand.class, path);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DisableSharingCommand.class, path);
        nuxeoController.executeNuxeoCommand(command);
    }

}
