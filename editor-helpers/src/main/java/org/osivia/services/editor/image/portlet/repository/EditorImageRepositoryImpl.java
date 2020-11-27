package org.osivia.services.editor.image.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.portal.core.web.IWebIdService;
import org.osivia.services.editor.common.repository.CommonRepositoryImpl;
import org.osivia.services.editor.image.portlet.repository.command.SearchImageDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Editor image portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepositoryImpl
 * @see EditorImageRepository
 */
@Repository
public class EditorImageRepositoryImpl extends CommonRepositoryImpl implements EditorImageRepository {

    /**
     * WebId Nuxeo document property.
     */
    private static final String WEB_ID_PROPERTY = "ttc:webid";

    /**
     * Nuxeo document URL prefix.
     */
    private static final String DOCUMENT_URL_PREFIX = "/nuxeo/web/";
    /**
     * Nuxeo document URL suffix.
     */
    private static final String DOCUMENT_URL_SUFFIX = "?content=Original";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public EditorImageRepositoryImpl() {
        super();
    }


    @Override
    public List<Document> search(PortalControllerContext portalControllerContext, String filter) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(SearchImageDocumentsCommand.class, filter);

        Documents result = (Documents) nuxeoController.executeNuxeoCommand(command);

        return result.list();
    }


    @Override
    public String getImageDocumentUrl(PortalControllerContext portalControllerContext, String path) {
        // Document
        Document document = this.getDocument(portalControllerContext, path);

        // WebId
        String webId = document.getString(WEB_ID_PROPERTY);

        return DOCUMENT_URL_PREFIX + webId + DOCUMENT_URL_SUFFIX;
    }


    @Override
    public CMSBinaryContent getImagePreviewBinaryContent(PortalControllerContext portalControllerContext, String webId, String content) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Fetch path
        String fetchPath = IWebIdService.FETCH_PATH_PREFIX + webId;

        return nuxeoController.fetchPicture(fetchPath, content);
    }

}
