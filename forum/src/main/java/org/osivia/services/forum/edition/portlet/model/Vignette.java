package org.osivia.services.forum.edition.portlet.model;

import org.osivia.services.forum.util.model.ForumFile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Vignette java-bean.
 *
 * @author Cédric Krommenhoek
 * @see ForumFile
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Vignette extends ForumFile {

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
