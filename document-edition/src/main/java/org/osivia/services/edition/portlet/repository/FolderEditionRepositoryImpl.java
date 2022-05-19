package org.osivia.services.edition.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.FolderEditionForm;
import org.springframework.stereotype.Repository;

/**
 * Folder edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionRepositoryImpl
 * @see FolderEditionForm
 */
@Repository
public class FolderEditionRepositoryImpl extends DocumentEditionRepositoryImpl<FolderEditionForm> {

    /**
     * Constructor.
     */
    public FolderEditionRepositoryImpl() {
        super();
    }


    @Override
    public Class<FolderEditionForm> getParameterizedType() {
        return FolderEditionForm.class;
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return "Folder".equals(documentType);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "folder";
    }

}
