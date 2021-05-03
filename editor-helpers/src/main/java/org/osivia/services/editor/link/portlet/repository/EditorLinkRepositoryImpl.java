package org.osivia.services.editor.link.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
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


    @Override
    public Document getDocumentFromUrl(PortalControllerContext portalControllerContext, String url) {
        Document document;

        if (StringUtils.startsWith(url, DOCUMENT_URL_PREFIX)) {
            // Web identifier
            String webId = StringUtils.substringAfter(url, DOCUMENT_URL_PREFIX);
            // Path
            String path = NuxeoController.webIdToFetchPath(webId);

            try {
                document = this.getDocument(portalControllerContext, path);
            } catch (Exception e) {
                document = null;
            }
        } else {
            document = null;
        }

        return document;
    }

}
