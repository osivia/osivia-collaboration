package org.osivia.services.editor.link.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;

import javax.portlet.PortletException;
import java.util.Map;

/**
 * Editor link portlet repository interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public interface EditorLinkRepository extends CommonRepository {

    /**
     * Get document URL from webId.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   webId
     * @return URL
     */
    String getDocumentUrl(PortalControllerContext portalControllerContext, String webId) throws PortletException;

}
