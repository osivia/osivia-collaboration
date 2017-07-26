package org.osivia.services.forum.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import java.io.File;

/**
 * Vignette java-bean.
 *
 * @author Cédric Krommenhoek
 * @see UploadedObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Vignette extends UploadedObject {

    /** Original URL. */
    private String url;
    /** Uploaded multipart file. */
    private MultipartFile upload;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public Vignette() {
        super();
    }


    /**
     * Getter for url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for upload.
     *
     * @return the upload
     */
    public MultipartFile getUpload() {
        return upload;
    }

    /**
     * Setter for upload.
     *
     * @param upload the upload to set
     */
    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    /**
     * Getter for deleted.
     *
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Setter for deleted.
     *
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
