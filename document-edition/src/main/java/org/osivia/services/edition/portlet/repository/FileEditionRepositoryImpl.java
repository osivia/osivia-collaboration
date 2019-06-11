package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.repository.command.ImportFileCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see FileEditionForm
 */
@Repository("File")
public class FileEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<FileEditionForm> {

    /**
     * File binary Nuxeo document property.
     */
    private static final String BINARY_PROPERTY = "file:content";
    /**
     * File binary name Nuxeo document property.
     */
    private static final String BINARY_NAME_PROPERTY = BINARY_PROPERTY + "/name";


    /**
     * Required primary types.
     */
    private final Map<String, String> requiredPrimaryTypes;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public FileEditionRepositoryImpl() {
        super();

        // Required primary types
        this.requiredPrimaryTypes = new ConcurrentHashMap<>();
        this.requiredPrimaryTypes.put("Picture", "image");
        this.requiredPrimaryTypes.put("Audio", "audio");
        this.requiredPrimaryTypes.put("Video", "video");
    }


    @Override
    public FileEditionForm getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException {
        return super.getForm(portalControllerContext, windowProperties, FileEditionForm.class);
    }


    @Override
    protected void customizeForm(PortalControllerContext portalControllerContext, Document document, FileEditionForm form) {
        // Required primary type
        String requiredPrimaryType = this.requiredPrimaryTypes.get(document.getType());
        form.setRequiredPrimaryType(requiredPrimaryType);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "file";
    }


    @Override
    public void validate(FileEditionForm form, Errors errors) {
        super.validate(form, errors);

        if (form.isCreation()) {
            // Creation
            if (form.getTemporaryFile() == null) {
                // Binary file is mandatory
                errors.rejectValue("upload", "NotEmpty");
            }
        } else {
            // Edition
            if ((form.getTemporaryFile() != null) && StringUtils.isNotEmpty(form.getRequiredPrimaryType())) {
                // Check primary type
                MimeType mimeType = form.getTemporaryFileMimeType();

                if (mimeType == null) {
                    // Unknown MIME type
                    errors.rejectValue("upload", "UnknownFileType");
                } else if (!StringUtils.equals(form.getRequiredPrimaryType(), mimeType.getPrimaryType())) {
                    // Invalid primary type
                    errors.rejectValue("upload", "InvalidFileType");
                }
            }
        }
    }

    @Override
    public void upload(PortalControllerContext portalControllerContext, FileEditionForm form) throws IOException {
        // Delete previous temporary file
        if (form.getTemporaryFile() != null) {
            form.getTemporaryFile().delete();
        }

        // Upload
        MultipartFile upload = form.getUpload();
        File temporaryFile = File.createTempFile("document-edition-file-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        form.setTemporaryFile(temporaryFile);
        form.setTemporaryFileName(upload.getOriginalFilename());

        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }
        form.setTemporaryFileMimeType(mimeType);

        // Title
        if (StringUtils.isBlank(form.getTitle())) {
            form.setTitle(form.getTemporaryFileName());
        }
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, FileEditionForm form) {
        // Delete previous temporary file
        if (form.getTemporaryFile() != null) {
            form.getTemporaryFile().delete();
        }


        // Update model
        form.setTemporaryFile(null);
        form.setTemporaryFileName(null);
        form.setTemporaryFileMimeType(null);
    }


    @Override
    protected void customizeProperties(PortalControllerContext portalControllerContext, FileEditionForm form, PropertyMap properties, Map<String, Blob> binaries) {
        if (form.getTemporaryFile() == null) {
            properties.set(BINARY_NAME_PROPERTY, form.getTitle());
        } else {
            // File
            File file = form.getTemporaryFile();
            // File name
            String name = form.getTitle();
            // File content type
            String contentType;
            if (form.getTemporaryFileMimeType() == null) {
                contentType = null;
            } else {
                contentType = form.getTemporaryFileMimeType().toString();
            }

            FileBlob blob = new FileBlob(file, name, contentType);
            blob.setFileName(form.getTitle());
            binaries.put(BINARY_PROPERTY, blob);
        }
    }


    @Override
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, Blob> binaries) throws PortletException {
        // File binary
        Blob binary = binaries.get(BINARY_PROPERTY);

        Document document;

        if (binary == null) {
            throw new PortletException("Empty file");
        } else {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(ImportFileCommand.class, parentPath, binary);

            document = (Document) nuxeoController.executeNuxeoCommand(command);
        }

        return document;
    }

}
