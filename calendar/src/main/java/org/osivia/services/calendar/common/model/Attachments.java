package org.osivia.services.calendar.common.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Attachments java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Attachments {

    /** Attachments original files. */
    private List<Attachment> files;
    /** Attachments uploaded multipart files. */
    private List<MultipartFile> upload;


    /**
     * Constructor.
     */
    public Attachments() {
        super();
    }


    /**
     * Getter for files.
     * 
     * @return the files
     */
    public List<Attachment> getFiles() {
        return files;
    }

    /**
     * Setter for files.
     * 
     * @param files the files to set
     */
    public void setFiles(List<Attachment> files) {
        this.files = files;
    }

    /**
     * Getter for upload.
     * 
     * @return the upload
     */
    public List<MultipartFile> getUpload() {
        return upload;
    }

    /**
     * Setter for upload.
     * 
     * @param upload the upload to set
     */
    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

}
