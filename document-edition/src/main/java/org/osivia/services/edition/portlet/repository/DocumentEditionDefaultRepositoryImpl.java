package org.osivia.services.edition.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.springframework.stereotype.Repository;

/**
 * Document edition default repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionRepositoryImpl
 * @see AbstractDocumentEditionForm
 * @see DocumentEditionDefaultRepository
 */
@Repository
public class DocumentEditionDefaultRepositoryImpl extends DocumentEditionRepositoryImpl<AbstractDocumentEditionForm> implements DocumentEditionDefaultRepository {

    /**
     * Constructor.
     */
    public DocumentEditionDefaultRepositoryImpl() {
        super();
    }


    @Override
    public Class<AbstractDocumentEditionForm> getParameterizedType() {
        throw new RuntimeException("Access denied to default implementation");
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        throw new RuntimeException("Access denied to default implementation");
    }

}
