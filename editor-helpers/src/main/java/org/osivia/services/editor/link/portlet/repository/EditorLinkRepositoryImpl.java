package org.osivia.services.editor.link.portlet.repository;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.repository.CommonRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Editor link portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepositoryImpl
 * @see EditorLinkRepository
 */
@Repository
public class EditorLinkRepositoryImpl extends CommonRepositoryImpl implements EditorLinkRepository {

    /**
     * Constructor.
     */
    public EditorLinkRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentUrl(PortalControllerContext portalControllerContext, String webId) {
        return DOCUMENT_URL_PREFIX + webId;
    }

}
