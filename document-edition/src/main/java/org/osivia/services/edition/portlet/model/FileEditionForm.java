package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * Required primary type.
     */
    private String requiredPrimaryType;
    /** Multiple files indicator. */
    private boolean multiple;

    /**
     * Upload.
     */
    private MultipartFile upload;
    /**
     * Temporary file.
     */
    private TemporaryFile temporaryFile;

    /**
     * Uploads.
     */
    private List<MultipartFile> uploads;
    /**
     * Temporary files.
     */
    private List<TemporaryFile> temporaryFiles;


    /**
     * Constructor.
     */
    public FileEditionForm() {
        super();
    }


    public String getRequiredPrimaryType() {
        return requiredPrimaryType;
    }

    public void setRequiredPrimaryType(String requiredPrimaryType) {
        this.requiredPrimaryType = requiredPrimaryType;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    public TemporaryFile getTemporaryFile() {
        return temporaryFile;
    }

    public void setTemporaryFile(TemporaryFile temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public List<MultipartFile> getUploads() {
        return uploads;
    }

    public void setUploads(List<MultipartFile> uploads) {
        this.uploads = uploads;
    }

    public List<TemporaryFile> getTemporaryFiles() {
        return temporaryFiles;
    }

    public void setTemporaryFiles(List<TemporaryFile> temporaryFiles) {
        this.temporaryFiles = temporaryFiles;
    }
}
