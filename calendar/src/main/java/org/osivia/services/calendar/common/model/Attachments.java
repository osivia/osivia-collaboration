package org.osivia.services.calendar.common.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Attachments java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Attachments {

    /**
     * Attachments original files.
     */
    private List<Attachment> files;
    /**
     * Attachments uploaded multipart files.
     */
    private List<MultipartFile> upload;
    /**
     * Attachments upload max size.
     */
    private long maxSize;


    /**
     * Constructor.
     */
    public Attachments() {
        super();
    }


    public List<Attachment> getFiles() {
        return files;
    }

    public void setFiles(List<Attachment> files) {
        this.files = files;
    }

    public List<MultipartFile> getUpload() {
        return upload;
    }

    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }
}
