package org.osivia.services.forum.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.services.forum.edition.portlet.model.*;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Forum edition Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumEditionCommand implements INuxeoCommand {

    /** Show in menu Nuxeo document property. */
    private static final String SHOW_IN_MENU_PROPERTY = "ttc:showInMenu";


    /** Forum edition form. */
    private final ForumEditionForm form;
    /** Forum edition options. */
    private final ForumEditionOptions options;


    /**
     * Constructor.
     *
     * @param form    forum edition form
     * @param options forum edition options
     */
    public ForumEditionCommand(ForumEditionForm form, ForumEditionOptions options) {
        super();
        this.form = form;
        this.options = options;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Edition mode
        ForumEditionMode mode = this.options.getMode();

        // Document
        Document document;

        if (ForumEditionMode.CREATION.equals(mode)) {
            document = this.create(nuxeoSession);
        } else if (ForumEditionMode.EDITION.equals(mode)) {
            document = this.edit(nuxeoSession);
        } else {
            document = null;
        }

        return document;
    }


    @Override
    public String getId() {
        return null;
    }


    /**
     * Create document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document create(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document type
        DocumentType documentType = this.options.getDocumentType();


        // Parent
        DocRef parent = new DocRef(this.options.getParentPath());
        // Type
        String type = documentType.getName();
        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set(ForumEditionRepository.TITLE_PROPERTY, this.form.getTitle());
        if (StringUtils.isNotBlank(this.form.getDescription())) {
            properties.set(ForumEditionRepository.DESCRIPTION_PROPERTY, this.form.getDescription());
        }
        if (ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(type)) {
            properties.set(ForumEditionRepository.MESSAGE_PROPERTY, this.form.getMessage());
        }
        properties.set(SHOW_IN_MENU_PROPERTY, documentType.isNavigable());


        // Creation
        Document document = documentService.createDocument(parent, type, null, properties, true);


        // Vignette
        Vignette vignette = this.form.getVignette();
        if (vignette.getTemporaryFile() != null) {
            // File blob
            Blob blob = new FileBlob(vignette.getTemporaryFile());

            documentService.setBlob(document, blob, ForumEditionRepository.VIGNETTE_PROPERTY);

            // Delete temporary file
            vignette.getTemporaryFile().delete();
        }


        // Attachments
        if (ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(type)) {
            Attachments attachments = this.form.getAttachments();

            // Set blobs
            this.setBlobs(documentService, document, attachments);
        }

        return document;
    }


    /**
     * Edit document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document edit(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);


        // Document
        Document document = this.options.getDocument();

        // Update document properties
        documentService.setProperty(document, ForumEditionRepository.TITLE_PROPERTY, this.form.getTitle());
        documentService.setProperty(document, ForumEditionRepository.DESCRIPTION_PROPERTY, this.form.getDescription());
        if (ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(document.getType())) {
            documentService.setProperty(document, ForumEditionRepository.MESSAGE_PROPERTY, this.form.getMessage());
        }


        // Vignette
        Vignette vignette = this.form.getVignette();
        // Temporary file
        File temporaryFile = vignette.getTemporaryFile();
        if (temporaryFile != null) {
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(document, blob, ForumEditionRepository.VIGNETTE_PROPERTY);

            // Delete temporary file
            temporaryFile.delete();
        } else if (vignette.isDeleted()) {
            documentService.removeBlob(document, ForumEditionRepository.VIGNETTE_PROPERTY);
        }


        // Attachments
        if (ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(document.getType())) {
            Attachments attachments = this.form.getAttachments();

            // Remove blobs
            for (Integer index : attachments.getDeletedBlobIndexes()) {
                StringBuilder xpath = new StringBuilder();
                xpath.append(ForumEditionRepository.ATTACHMENTS_PROPERTY);
                xpath.append("/item[");
                xpath.append(index);
                xpath.append("]");

                documentService.removeBlob(document, xpath.toString());
            }

            // Set blobs
            this.setBlobs(documentService, document, attachments);
        }

        return document;
    }


    /**
     * Set blobs.
     *
     * @param documentService Nuxeo document service
     * @param document        Nuxeo document
     * @param attachments     attachments
     * @throws Exception
     */
    private void setBlobs(DocumentService documentService, Document document, Attachments attachments) throws Exception {
        // Attachment files
        List<AttachmentFile> files = attachments.getFiles();

        // Blobs
        List<Blob> blobs = new ArrayList<>(files.size());

        for (AttachmentFile file : files) {
            // Temporary file
            File temporaryFile = file.getTemporaryFile();

            if (temporaryFile != null) {
                // File name
                String fileName = file.getFileName();
                // Mime type
                String mimeType = file.getMimeType().getBaseType();

                // File blob
                Blob blob = new FileBlob(temporaryFile, fileName, mimeType);

                blobs.add(blob);
            }
        }

        if (!blobs.isEmpty()) {
            documentService.setBlobs(document, new Blobs(blobs), ForumEditionRepository.ATTACHMENTS_PROPERTY);

            // Delete temporary files
            for (AttachmentFile file : files) {
                if (file.getTemporaryFile() != null) {
                    file.getTemporaryFile().delete();
                }
            }
        }
    }

}
