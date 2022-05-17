package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionMetadata;
import org.osivia.services.edition.portlet.model.ExistingFile;
import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Metadata edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionMetadataRepository
 */
@Repository
public class DocumentEditionMetadataRepositoryImpl implements DocumentEditionMetadataRepository {

    /**
     * Description Nuxeo document property.
     */
    protected static final String DESCRIPTION_PROPERTY = "dc:description";
    /**
     * Vignette Nuxeo document property.
     */
    protected static final String VIGNETTE_PROPERTY = "ttc:vignette";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DocumentEditionMetadataRepositoryImpl() {
        super();
    }


    @Override
    public DocumentEditionMetadata getMetadata(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Metadata
        DocumentEditionMetadata metadata = this.applicationContext.getBean(DocumentEditionMetadata.class);

        // Description
        String description = document.getString(DESCRIPTION_PROPERTY);
        metadata.setDescription(description);

        // Vignette
        ExistingFile existingFile;
        PropertyMap vignettePropertyMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
        if ((vignettePropertyMap == null) || StringUtils.isEmpty(vignettePropertyMap.getString("data"))) {
            existingFile = null;
        } else {
            existingFile = this.applicationContext.getBean(ExistingFile.class);

            // URL
            String url = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
            existingFile.setDownloadUrl(url);
        }
        metadata.setVignette(existingFile);

        return metadata;
    }


    @Override
    public void uploadVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException {
        // Delete previous temporary file
        this.deleteVignetteTemporaryFile(metadata);

        // Upload
        MultipartFile upload = metadata.getVignetteUpload();
        File file = File.createTempFile("uploaded-vignette-", ".tmp");
        file.deleteOnExit();
        upload.transferTo(file);

        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }

        // Update temporary file
        UploadTemporaryFile temporaryFile = this.applicationContext.getBean(UploadTemporaryFile.class);
        temporaryFile.setFile(file);
        temporaryFile.setFileName(upload.getOriginalFilename());
        temporaryFile.setMimeType(mimeType);
        metadata.setVignetteTemporaryFile(temporaryFile);

        metadata.setVignetteDeleted(false);
    }


    @Override
    public void deleteVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException {
        // Delete previous temporary file
        this.deleteVignetteTemporaryFile(metadata);

        metadata.setVignetteTemporaryFile(null);
        metadata.setVignetteDeleted(true);
    }


    @Override
    public void restoreVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException {
        // Delete previous temporary file
        this.deleteVignetteTemporaryFile(metadata);

        metadata.setVignetteTemporaryFile(null);
        metadata.setVignetteDeleted(false);
    }


    /**
     * Delete vignette temporary file.
     *
     * @param metadata document metadata
     */
    protected void deleteVignetteTemporaryFile(DocumentEditionMetadata metadata) {
        UploadTemporaryFile temporaryFile = metadata.getVignetteTemporaryFile();
        if ((temporaryFile != null) && (temporaryFile.getFile() != null) && !temporaryFile.getFile().delete()) {
            temporaryFile.getFile().deleteOnExit();
        }
    }


    @Override
    public void validate(DocumentEditionMetadata metadata, Errors errors) {
        // Vignette temporary file
        UploadTemporaryFile temporaryFile = metadata.getVignetteTemporaryFile();

        if (temporaryFile != null) {
            // Primary MIME type
            String primaryType;
            if (temporaryFile.getMimeType() == null) {
                primaryType = null;
            } else {
                primaryType = temporaryFile.getMimeType().getPrimaryType();
            }

            if (!StringUtils.equals("image", primaryType)) {
                errors.rejectValue("metadata.vignetteUpload", "InvalidFileType");
            }
        }
    }


    @Override
    public void customizeProperties(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException {
        // Description
        String description = metadata.getDescription();
        properties.set(DESCRIPTION_PROPERTY, description);

        // Vignette
        if ((metadata.getVignetteTemporaryFile() != null) && (metadata.getVignetteTemporaryFile().getFile() != null)) {
            File file = metadata.getVignetteTemporaryFile().getFile();
            FileBlob blob = new FileBlob(file);
            binaries.put(VIGNETTE_PROPERTY, Collections.singletonList(blob));
        } else if (metadata.isVignetteDeleted()) {
            binaries.put(VIGNETTE_PROPERTY, null);
        }
    }

}
