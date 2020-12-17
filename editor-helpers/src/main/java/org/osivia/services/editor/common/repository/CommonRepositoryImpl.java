package org.osivia.services.editor.common.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

import javax.portlet.PortletException;

/**
 * Portlet common repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public abstract class CommonRepositoryImpl implements CommonRepository {

    /**
     * Constructor.
     */
    public CommonRepositoryImpl() {
        super();
    }


    @Override
    public Document getDocument(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

        // Disable cache
        documentContext.reload();

        return documentContext.getDocument();
    }

}
