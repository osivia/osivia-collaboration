package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.FilesCreationForm;
import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
import org.osivia.services.edition.portlet.repository.command.ImportFilesCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Files creation portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see FilesCreationForm
 */
@Repository
public class FilesCreationRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<FilesCreationForm> {

    /**
     * Accepted document types.
     */
    private final Set<String> acceptedDocumentTypes;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public FilesCreationRepositoryImpl() {
        super();

        // Accepted document types
        this.acceptedDocumentTypes = Stream.of("File", "Picture", "Audio", "Video").collect(Collectors.toSet());
    }


    @Override
    public Class<FilesCreationForm> getParameterizedType() {
        return FilesCreationForm.class;
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return creation && this.acceptedDocumentTypes.contains(documentType);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "files";
    }


    @Override
    protected void customizeValidation(FilesCreationForm form, Errors errors) {
        // Required primary MIME-type
        String requiredPrimaryType;
        switch (form.getWindowProperties().getDocumentType()) {
            case "Audio":
                requiredPrimaryType = "audio";
                break;
            case "Picture":
                requiredPrimaryType = "image";
                break;
            case "Video":
                requiredPrimaryType = "video";
                break;
            default:
                requiredPrimaryType = null;
        }

        if (CollectionUtils.isEmpty(form.getTemporaryFiles())) {
            // Binary file is mandatory
            errors.rejectValue("upload", "NotEmpty");
        } else if (StringUtils.isNotEmpty(requiredPrimaryType)) {
            for (UploadTemporaryFile temporaryFile : form.getTemporaryFiles()) {
                MimeType mimeType = temporaryFile.getMimeType();
                if ((mimeType == null) || !StringUtils.equals(requiredPrimaryType, mimeType.getPrimaryType())) {
                    // Invalid picture type
                    errors.rejectValue("upload", "InvalidFileType", new Object[]{temporaryFile.getFileName()}, null);
                }
            }
        }
    }


    @Override
    protected void customizeUpload(PortalControllerContext portalControllerContext, FilesCreationForm form) throws IOException {
        if (CollectionUtils.isNotEmpty(form.getUpload())) {
            // Temporary files
            List<UploadTemporaryFile> temporaryFiles = form.getTemporaryFiles();
            if (CollectionUtils.isEmpty(temporaryFiles)) {
                temporaryFiles = new ArrayList<>(form.getUpload().size());
                form.setTemporaryFiles(temporaryFiles);
            }

            for (MultipartFile upload : form.getUpload()) {
                UploadTemporaryFile temporaryFile = this.createTemporaryFile(upload);
                temporaryFiles.add(temporaryFile);
            }
        }
    }


    @Override
    protected void customizeRestore(PortalControllerContext portalControllerContext, FilesCreationForm form) {
        if (CollectionUtils.isNotEmpty(form.getTemporaryFiles())) {
            // Portlet request
            PortletRequest request = portalControllerContext.getRequest();

            int index = NumberUtils.toInt(request.getParameter("restore"), -1);
            if (index < 0) {
                // Delete all temporary files
                for (UploadTemporaryFile temporaryFile : form.getTemporaryFiles()) {
                    FileUtils.deleteQuietly(temporaryFile.getFile());
                }

                // Update model
                form.getTemporaryFiles().clear();
            } else if (index < form.getTemporaryFiles().size()) {
                // Delete selected temporary file
                UploadTemporaryFile temporaryFile = form.getTemporaryFiles().get(index);
                FileUtils.deleteQuietly(temporaryFile.getFile());

                // Update model
                form.getTemporaryFiles().remove(index);
            }
        }
    }


    @Override
    protected void customizeProperties(PortalControllerContext portalControllerContext, FilesCreationForm form, PropertyMap properties, Map<String, List<Blob>> binaries) {
        if (CollectionUtils.isNotEmpty(form.getTemporaryFiles())) {
            List<Blob> blobs = new ArrayList<>();

            for (UploadTemporaryFile temporaryFile : form.getTemporaryFiles()) {
                // File name
                String name = Normalizer.normalize(temporaryFile.getFileName(), Normalizer.Form.NFC);

                // File content type
                String contentType;
                if (temporaryFile.getMimeType() == null) {
                    contentType = null;
                } else {
                    contentType = temporaryFile.getMimeType().toString();
                }

                FileBlob blob = new FileBlob(temporaryFile.getFile(), name, contentType);
                blobs.add(blob);
            }

            binaries.put(StringUtils.EMPTY, blobs);
        }
    }


    @Override
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException {
        // File binaries
        List<Blob> blobs = binaries.get(StringUtils.EMPTY);
        if (CollectionUtils.isEmpty(blobs)) {
            throw new PortletException("Empty files");
        }

        // Nuxeo command
        ImportFilesCommand command = this.applicationContext.getBean(ImportFilesCommand.class);
        command.setParentPath(parentPath);
        command.setBinaries(blobs);

        Document document = (Document) nuxeoController.executeNuxeoCommand(command);
        if (blobs.size() > 1) {
            document = null; // Command result not relevant
        }

        return document;
    }

}
