package org.osivia.services.editor.image.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.portal.core.web.IWebIdService;
import org.osivia.services.editor.common.repository.CommonRepositoryImpl;
import org.osivia.services.editor.image.portlet.model.AttachedImage;
import org.osivia.services.editor.image.portlet.model.SearchScope;
import org.osivia.services.editor.image.portlet.repository.command.AddAttachedImageCommand;
import org.osivia.services.editor.image.portlet.repository.command.DeleteAttachedImageCommand;
import org.osivia.services.editor.image.portlet.repository.command.SearchImageDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
    public SortedSet<AttachedImage> getAttachedImages(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document
        Document document = this.getDocument(portalControllerContext, path);

        // Attached images
        SortedSet<AttachedImage> attachedImages;

        PropertyList list = document.getProperties().getList(ATTACHED_IMAGES_PROPERTY);
        if ((list == null) || list.isEmpty()) {
            attachedImages = null;
        } else {
            attachedImages = new TreeSet<>();

            for (int i = 0; i < list.size(); i++) {
                PropertyMap map = list.getMap(i).getMap("file");
                // File name
                String fileName = map.getString("name");
                // URL
                String url = nuxeoController.createAttachedPictureLink(path, String.valueOf(i), fileName);

                // Attached image
                AttachedImage attachedImage = this.applicationContext.getBean(AttachedImage.class);
                attachedImage.setIndex(i);
                attachedImage.setFileName(fileName);
                attachedImage.setUrl(url);

                attachedImages.add(attachedImage);
            }
        }

        return attachedImages;
    }


    @Override
    public void addAttachedImage(PortalControllerContext portalControllerContext, String path, File temporaryFile, String fileName, String contentType) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(AddAttachedImageCommand.class, path, temporaryFile, fileName, contentType);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public String getAttachedImageUrl(PortalControllerContext portalControllerContext, AttachedImage attachedImage) {
        return ATTACHED_IMAGE_URL_PREFIX + ATTACHED_IMAGES_PROPERTY + "/" + attachedImage.getIndex() + "/file/" + attachedImage.getFileName();
    }


    @Override
    public void deleteAttachedImage(PortalControllerContext portalControllerContext, String path, int index) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteAttachedImageCommand.class, path, index);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public List<Document> search(PortalControllerContext portalControllerContext, String basePath, String filter, SearchScope scope) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(SearchImageDocumentsCommand.class, basePath, filter, scope);

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
    public CMSBinaryContent getImageDocumentPreviewBinaryContent(PortalControllerContext portalControllerContext, String webId, String content) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Fetch path
        String fetchPath = IWebIdService.FETCH_PATH_PREFIX + webId;

        return nuxeoController.fetchPicture(fetchPath, content);
    }


    @Override
    public CMSBinaryContent getAttachedImagePreviewBinaryContent(PortalControllerContext portalControllerContext, int index) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current document path
        String path = nuxeoController.getContentPath();

        return nuxeoController.fetchAttachedPicture(path, String.valueOf(index));
    }

}
