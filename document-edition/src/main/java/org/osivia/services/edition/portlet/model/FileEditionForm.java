package org.osivia.services.edition.portlet.model;

import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import java.io.File;

/**
 * File edition form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileEditionForm extends AbstractDocumentEditionForm {

    /**
     * Existing file.
     */
    private ExistingFile existingFile;
    /**
     * Required primary type.
     */
    private String requiredPrimaryType;
    /**
     * Upload.
     */
    private MultipartFile upload;
    /**
     * Upload temporary file.
     */
    private UploadTemporaryFile temporaryFile;


    /**
     * Constructor.
     */
    public FileEditionForm() {
        super();
    }


    public ExistingFile getExistingFile() {
        return existingFile;
    }

    public void setExistingFile(ExistingFile existingFile) {
        this.existingFile = existingFile;
    }

    public String getRequiredPrimaryType() {
        return requiredPrimaryType;
    }

    public void setRequiredPrimaryType(String requiredPrimaryType) {
        this.requiredPrimaryType = requiredPrimaryType;
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
}
