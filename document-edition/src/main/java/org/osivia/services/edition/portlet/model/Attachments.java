package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Attachments.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Attachments {

    /**
     * Existing files.
     */
    private List<ExistingFile> existingFiles;
    /**
     * Upload.
     */
    private List<MultipartFile> upload;
    /**
     * Upload temporary files.
     */
    private List<UploadTemporaryFile> uploadTemporaryFiles;


    /**
     * Constructor.
     */
    public Attachments() {
        super();
    }


    public List<ExistingFile> getExistingFiles() {
        return existingFiles;
    }

    public void setExistingFiles(List<ExistingFile> existingFiles) {
        this.existingFiles = existingFiles;
    }

    public List<MultipartFile> getUpload() {
        return upload;
    }

    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    public List<UploadTemporaryFile> getUploadTemporaryFiles() {
        return uploadTemporaryFiles;
    }

    public void setUploadTemporaryFiles(List<UploadTemporaryFile> uploadTemporaryFiles) {
        this.uploadTemporaryFiles = uploadTemporaryFiles;
    }
}
