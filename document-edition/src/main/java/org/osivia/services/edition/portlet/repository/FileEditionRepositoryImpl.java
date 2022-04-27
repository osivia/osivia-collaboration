package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.repository.command.ImportFileCommand;
import org.osivia.services.edition.portlet.repository.command.ImportFilesCommand;
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
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see FileEditionForm
 */
@Repository
public class FileEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<FileEditionForm> {

    /**
     * File binary Nuxeo document property.
     */
    private static final String BINARY_PROPERTY = "file:content";
    /**
     * File binary name Nuxeo document property.
     */
    private static final String BINARY_NAME_PROPERTY = "file:filename";


    /**
     * Accepted document types.
     */
    private final Set<String> acceptedDocumentTypes;
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

        // Accepted document types
        this.acceptedDocumentTypes = Stream.of("File", "Picture", "Audio", "Video").collect(Collectors.toSet());

        // Required primary types
        this.requiredPrimaryTypes = new ConcurrentHashMap<>();
        this.requiredPrimaryTypes.put("Picture", "image");
        this.requiredPrimaryTypes.put("Audio", "audio");
        this.requiredPrimaryTypes.put("Video", "video");
    }


    @Override
    public Class<FileEditionForm> getParameterizedType() {
        return FileEditionForm.class;
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return !creation && this.acceptedDocumentTypes.contains(documentType);
    }


    @Override
    protected void customizeForm(PortalControllerContext portalControllerContext, Document document, FileEditionForm form) {
        // Required primary type
        String requiredPrimaryType = this.requiredPrimaryTypes.get(document.getType());
        form.setRequiredPrimaryType(requiredPrimaryType);
        form.setOriginalFileName(document.getProperties().getString(getBinaryNameProperty()));
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "file";
    }


    @Override
    public void customizeValidation(FileEditionForm form, Errors errors) {
        super.customizeValidation(form, errors);

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
    public void customizeUpload(PortalControllerContext portalControllerContext, FileEditionForm form) throws IOException {
        // Delete previous temporary file
        if (form.getTemporaryFile() != null && !form.getTemporaryFile().delete()) {
            form.getTemporaryFile().deleteOnExit();
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
    public void customizeRestore(PortalControllerContext portalControllerContext, FileEditionForm form) {
        // Delete previous temporary file
        if (form.getTemporaryFile() != null && !form.getTemporaryFile().delete()) {
            form.getTemporaryFile().deleteOnExit();
        }

        // Update model
        form.setTemporaryFile(null);
        form.setTemporaryFileName(null);
        form.setTemporaryFileMimeType(null);
    }


    @Override
    protected void customizeProperties(PortalControllerContext portalControllerContext, FileEditionForm form, PropertyMap properties, Map<String, List<Blob>> binaries) {
        if (form.getTemporaryFile() != null) {
            // File
            File file = form.getTemporaryFile();
            // File content type
            String contentType;
            if (form.getTemporaryFileMimeType() == null) {
                contentType = null;
            } else {
                contentType = form.getTemporaryFileMimeType().toString();
            }

            String s = Normalizer.normalize(form.getTemporaryFileName(), Normalizer.Form.NFC);

            FileBlob blob = new FileBlob(file, s, contentType);
            binaries.put(BINARY_PROPERTY, Collections.singletonList(blob));
        }
    }


    @Override
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException {
        // File binaries
        List<Blob> blobs = binaries.get(BINARY_PROPERTY);
        if (CollectionUtils.isEmpty(blobs)) {
            throw new PortletException("Empty file");
        }

        // Nuxeo command
        ImportFilesCommand command = this.applicationContext.getBean(ImportFilesCommand.class);
        command.setParentPath(parentPath);
        command.setBinaries(blobs);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }

    protected String getBinaryProperty() {
    	return BINARY_PROPERTY;
    }

    protected String getBinaryNameProperty() {
    	return BINARY_NAME_PROPERTY;
    }
}
