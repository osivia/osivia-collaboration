package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Metadata.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentEditionMetadata {

    /**
     * Description.
     */
    private String description;
    /**
     * Vignette.
     */
    private ExistingFile vignette;
    /**
     * Vignette upload.
     */
    private MultipartFile vignetteUpload;
    /**
     * Vignette temporary file.
     */
    private UploadTemporaryFile vignetteTemporaryFile;
    /**
     * Vignette deleted indicator.
     */
    private boolean vignetteDeleted;


    /**
     * Constructor.
     */
    public DocumentEditionMetadata() {
        super();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExistingFile getVignette() {
        return vignette;
    }

    public void setVignette(ExistingFile vignette) {
        this.vignette = vignette;
    }

    public MultipartFile getVignetteUpload() {
        return vignetteUpload;
    }

    public void setVignetteUpload(MultipartFile vignetteUpload) {
        this.vignetteUpload = vignetteUpload;
    }

    public UploadTemporaryFile getVignetteTemporaryFile() {
        return vignetteTemporaryFile;
    }

    public void setVignetteTemporaryFile(UploadTemporaryFile vignetteTemporaryFile) {
        this.vignetteTemporaryFile = vignetteTemporaryFile;
    }

    public boolean isVignetteDeleted() {
        return vignetteDeleted;
    }

    public void setVignetteDeleted(boolean vignetteDeleted) {
        this.vignetteDeleted = vignetteDeleted;
    }
}
