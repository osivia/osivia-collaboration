package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.FolderEditionForm;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Folder edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see FolderEditionForm
 */
@Repository("Folder")
public class FolderEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<FolderEditionForm> {

    /**
     * Constructor.
     */
    public FolderEditionRepositoryImpl() {
        super();
    }


    @Override
    public FolderEditionForm getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException {
        return super.getForm(portalControllerContext, windowProperties, FolderEditionForm.class);
    }


    @Override
    void customizeForm(PortalControllerContext portalControllerContext, Document document, FolderEditionForm form) {
        // Do nothing
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "folder";
    }


    @Override
    void customizeProperties(PortalControllerContext portalControllerContext, FolderEditionForm form, PropertyMap properties) {
        // Do nothing
    }

}
