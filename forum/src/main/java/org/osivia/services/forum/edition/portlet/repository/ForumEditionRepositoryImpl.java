package org.osivia.services.forum.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.edition.portlet.model.AttachmentFile;
import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.ForumEditionOptions;
import org.osivia.services.forum.edition.portlet.repository.command.ForumEditionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.List;

/**
 * Forum edition repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see ForumEditionRepository
 */
@Repository
public class ForumEditionRepositoryImpl implements ForumEditionRepository {

    /** Nuxeo document request attribute. */
    private static final String DOCUMENT_REQUEST_ATTRIBUTE = "osivia.forum.edition.document";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public ForumEditionRepositoryImpl() {
        super();
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, ForumEditionForm form, ForumEditionOptions options) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ForumEditionCommand.class, form, options);
        Document document = (Document) nuxeoController.executeNuxeoCommand(command);

        options.setDocument(document);
    }


    @Override
    public Document getDocument(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Nuxeo document
        Document document = (Document) request.getAttribute(DOCUMENT_REQUEST_ATTRIBUTE);
        if (document == null) {
            // Nuxeo document context
            NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();

            document = documentContext.getDocument();

            request.setAttribute(DOCUMENT_REQUEST_ATTRIBUTE, document);
        }

        return document;
    }


    @Override
    public void fillDocumentProperties(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo document
        Document document = this.getDocument(portalControllerContext);

        form.setTitle(document.getTitle());
        form.setDescription(document.getString(DESCRIPTION_PROPERTY));

        // Vignette
        PropertyMap vignetteMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
        if ((vignetteMap != null) && !vignetteMap.isEmpty()) {
            String vignetteUrl = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
            form.getVignette().setUrl(vignetteUrl);
        }

        if (DOCUMENT_TYPE_THREAD.equals(document.getType())) {
            form.setMessage(document.getString(MESSAGE_PROPERTY));

            // Attachments
            PropertyList attachmentsList = document.getProperties().getList(ATTACHMENTS_PROPERTY);
            if ((attachmentsList != null) && !attachmentsList.isEmpty()) {
                List<AttachmentFile> files = form.getAttachments().getFiles();
                for (int i = 0; i < attachmentsList.size(); i++) {
                    // Attachment
                    PropertyMap attachmentMap = attachmentsList.getMap(i);
                    PropertyMap attachmentFileMap = attachmentMap.getMap("file");
                    String fileName = attachmentFileMap.getString("name");
                    MimeType mimeType;
                    try {
                        mimeType = new MimeType(attachmentFileMap.getString("mime-type"));
                    } catch (MimeTypeParseException e) {
                        mimeType = null;
                    }


                    AttachmentFile file = this.applicationContext.getBean(AttachmentFile.class);
                    file.setBlobIndex(i);
                    file.setFileName(fileName);
                    file.setMimeType(mimeType);

                    files.add(file);
                }
            }
        }
    }

}
