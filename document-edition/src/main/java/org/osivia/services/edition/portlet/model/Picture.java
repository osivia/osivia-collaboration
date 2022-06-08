package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Picture.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Picture {

    /**
     * Existing file.
     */
    private ExistingFile existingFile;
    /**
     * Upload.
     */
    private MultipartFile upload;
    /**
     * Temporary file.
     */
    private UploadTemporaryFile temporaryFile;
    /**
     * Deleted indicator.
     */
    private boolean deleted;


    /**
     * Constructor.
     */
    public Picture() {
        super();
    }


    public ExistingFile getExistingFile() {
        return existingFile;
    }

    public void setExistingFile(ExistingFile existingFile) {
        this.existingFile = existingFile;
    }

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    public UploadTemporaryFile getTemporaryFile() {
        return temporaryFile;
    }

    public void setTemporaryFile(UploadTemporaryFile temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
