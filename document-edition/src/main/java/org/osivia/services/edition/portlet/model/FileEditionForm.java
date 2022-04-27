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
     * Upload max size.
     */
    private final long maxSize;

    /**
     * Required primary type.
     */
    private String requiredPrimaryType;
    /**
     * Original file name.
     */
    private String originalFileName;
    /**
     * Upload.
     */
    private MultipartFile upload;
    /**
     * Temporary file.
     */
    private File temporaryFile;
    /**
     * Temporary file name.
     */
    private String temporaryFileName;
    /**
     * Temporary file MIME type.
     */
    private MimeType temporaryFileMimeType;


    /**
     * Constructor.
     */
    public FileEditionForm() {
        super();
        this.maxSize = DocumentEditionService.MAX_UPLOAD_SIZE;
    }


    public long getMaxSize() {
        return maxSize;
    }

    public String getRequiredPrimaryType() {
        return requiredPrimaryType;
    }

    public void setRequiredPrimaryType(String requiredPrimaryType) {
        this.requiredPrimaryType = requiredPrimaryType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    public File getTemporaryFile() {
        return temporaryFile;
    }

    public void setTemporaryFile(File temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public String getTemporaryFileName() {
        return temporaryFileName;
    }

    public void setTemporaryFileName(String temporaryFileName) {
        this.temporaryFileName = temporaryFileName;
    }

    public MimeType getTemporaryFileMimeType() {
        return temporaryFileMimeType;
    }

    public void setTemporaryFileMimeType(MimeType temporaryFileMimeType) {
        this.temporaryFileMimeType = temporaryFileMimeType;
    }
}
