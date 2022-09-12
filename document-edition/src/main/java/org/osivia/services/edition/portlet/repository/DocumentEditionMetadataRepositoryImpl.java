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
import org.osivia.services.edition.portlet.model.Picture;
import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.File;
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
public class DocumentEditionMetadataRepositoryImpl extends DocumentEditionCommonRepositoryImpl<DocumentEditionMetadata> implements DocumentEditionMetadataRepository {

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
    public DocumentEditionMetadata get(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Metadata
        DocumentEditionMetadata metadata = this.applicationContext.getBean(DocumentEditionMetadata.class);
        // Vignette
        Picture vignette = this.applicationContext.getBean(Picture.class);
        metadata.setVignette(vignette);

        if (document != null) {
            // Description
            String description = document.getString(DESCRIPTION_PROPERTY);
            metadata.setDescription(description);

            // Vignette existing file
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
            vignette.setExistingFile(existingFile);
        }

        return metadata;
    }


    @Override
    public void validate(DocumentEditionMetadata metadata, Errors errors) {
        // Vignette
        Picture vignette = metadata.getVignette();

        // Vignette temporary file
        UploadTemporaryFile temporaryFile = vignette.getTemporaryFile();

        if (temporaryFile != null) {
            // Primary MIME type
            String primaryType;
            if (temporaryFile.getMimeType() == null) {
                primaryType = null;
            } else {
                primaryType = temporaryFile.getMimeType().getPrimaryType();
            }

            if (!StringUtils.equals("image", primaryType)) {
                errors.rejectValue("metadata.vignette.upload", "InvalidFileType");
            }
        }
    }


    @Override
    public void customizeProperties(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata, boolean creation, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException {
        // Description
        if (!creation || StringUtils.isNotBlank(metadata.getDescription())) {
            properties.set(DESCRIPTION_PROPERTY, StringUtils.trimToNull(metadata.getDescription()));
        }

        // Vignette
        Picture vignette = metadata.getVignette();
        if ((vignette.getTemporaryFile() != null) && (vignette.getTemporaryFile().getFile() != null)) {
            File file = vignette.getTemporaryFile().getFile();
            FileBlob blob = new FileBlob(file);
            binaries.put(VIGNETTE_PROPERTY, Collections.singletonList(blob));
        } else if (vignette.isDeleted()) {
            binaries.put(VIGNETTE_PROPERTY, null);
        }
    }

}
