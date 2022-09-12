package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Files creation form.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilesCreationForm extends AbstractDocumentEditionForm {

    /**
     * Upload.
     */
    private List<MultipartFile> upload;
    /**
     * Temporary files.
     */
    private List<UploadTemporaryFile> temporaryFiles;


    /**
     * Constructor.
     */
    public FilesCreationForm() {
        super();
    }


    public List<MultipartFile> getUpload() {
        return upload;
    }

    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    public List<UploadTemporaryFile> getTemporaryFiles() {
        return temporaryFiles;
    }

    public void setTemporaryFiles(List<UploadTemporaryFile> temporaryFiles) {
        this.temporaryFiles = temporaryFiles;
    }
}
