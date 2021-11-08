package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.model.TemporaryFile;
import org.osivia.services.edition.portlet.repository.command.ImportFileCommand;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
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
import java.util.ArrayList;
import java.util.List;
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
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;

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
        FileEditionForm form = super.getForm(portalControllerContext, windowProperties, FileEditionForm.class);

        // Multiple files indicator
        boolean multiple = (StringUtils.isEmpty(windowProperties.getDocumentPath()) && windowProperties.isMultipleFiles());
        form.setMultiple(multiple);

        return form;
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
        if (form.isMultiple()) {
            // Multiple creation
            if (CollectionUtils.isEmpty(form.getTemporaryFiles())) {
                // Binary file is mandatory
                errors.rejectValue("uploads", "NotEmpty");
            }
        } else if (form.isCreation()) {
            // Creation
            super.validate(form, errors);

            if (form.getTemporaryFile() == null) {
                // Binary file is mandatory
                errors.rejectValue("upload", "NotEmpty");
            }
        } else {
            // Edition
            super.validate(form, errors);

            if ((form.getTemporaryFile() != null) && (form.getTemporaryFile().getFile() != null) && StringUtils.isNotEmpty(form.getRequiredPrimaryType())) {
                // Check primary type
                MimeType mimeType = form.getTemporaryFile().getMimeType();

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
        if (form.isMultiple()) {
            // Delete previous temporary files
            if (CollectionUtils.isNotEmpty(form.getTemporaryFiles())) {
                for (TemporaryFile temporaryFile : form.getTemporaryFiles()) {
                    if (temporaryFile.getFile() != null) {
                        temporaryFile.getFile().delete();
                    }
                }
                form.setTemporaryFiles(null);
            }

            // Uploads
            List<MultipartFile> uploads = form.getUploads();

            if (CollectionUtils.isNotEmpty(uploads)) {
                // Temporary files
                List<TemporaryFile> temporaryFiles = new ArrayList<>(uploads.size());
                form.setTemporaryFiles(temporaryFiles);

                for (MultipartFile upload : uploads) {
                    TemporaryFile temporaryFile = this.createTemporaryFile(upload);
                    temporaryFiles.add(temporaryFile);
                }
            }
        } else {
            // Delete previous temporary file
            if ((form.getTemporaryFile() != null) && (form.getTemporaryFile().getFile() != null)) {
                form.getTemporaryFile().getFile().delete();
            }

            // Upload
            MultipartFile upload = form.getUpload();
            TemporaryFile temporaryFile = this.createTemporaryFile(upload);
            form.setTemporaryFile(temporaryFile);

            // Title
            if (StringUtils.isBlank(form.getTitle())) {
                form.setTitle(temporaryFile.getName());
            }
        }
    }


    /**
     * Create temporary file.
     *
     * @param upload multipart file upload
     * @return temporary file
     */
    private TemporaryFile createTemporaryFile(MultipartFile upload) throws IOException {
        File file = File.createTempFile("document-edition-file-", ".tmp");
        file.deleteOnExit();
        upload.transferTo(file);

        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }

        // Temporary file
        TemporaryFile temporaryFile = this.applicationContext.getBean(TemporaryFile.class);
        temporaryFile.setFile(file);
        temporaryFile.setName(upload.getOriginalFilename());
        temporaryFile.setMimeType(mimeType);

        return temporaryFile;
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, FileEditionForm form) {
        // Delete previous temporary file
        if ((form.getTemporaryFile() != null) && (form.getTemporaryFile().getFile() != null)) {
            form.getTemporaryFile().getFile().delete();
        }


        // Update model
        form.setTemporaryFile(null);
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, FileEditionForm form) throws PortletException, IOException {
        if (form.isMultiple()) {
            if (CollectionUtils.isNotEmpty(form.getTemporaryFiles())) {
                for (TemporaryFile temporaryFile : form.getTemporaryFiles()) {
                    // Single file creation form
                    FileEditionForm singleFileForm = this.applicationContext.getBean(FileEditionForm.class);
                    singleFileForm.setCreation(true);
                    singleFileForm.setTitle(temporaryFile.getName());
                    singleFileForm.setTemporaryFile(temporaryFile);

                    super.save(portalControllerContext, singleFileForm);
                }
                
                // Window properties
                DocumentEditionWindowProperties windowProperties = this.service.getWindowProperties(portalControllerContext);
                // Parent path
                String parentPath = windowProperties.getParentDocumentPath();
                form.setParentPath(parentPath);
            }
        } else {
            super.save(portalControllerContext, form);
        }
    }


    @Override
    protected void customizeProperties(PortalControllerContext portalControllerContext, FileEditionForm form, PropertyMap properties, Map<String, Blob> binaries) {
        if ((form.getTemporaryFile() == null) || (form.getTemporaryFile().getFile() == null)) {
            properties.set(BINARY_NAME_PROPERTY, form.getTitle());
        } else {
            // File
            File file = form.getTemporaryFile().getFile();
            // File name
            String name = form.getTitle();
            // File content type
            String contentType;
            if (form.getTemporaryFile().getMimeType() == null) {
                contentType = null;
            } else {
                contentType = form.getTemporaryFile().getMimeType().toString();
            }

            FileBlob blob = new FileBlob(file, name, contentType);
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
