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

    /**
     * Max upload size.
     */
    private long maxUploadSize;
    /**
     * Uploaded multipart file.
     */
    private MultipartFile upload;
    /**
     * Deleted indicator.
     */
    private boolean deleted;


    /**
     * Constructor.
     */
    public Vignette() {
        super();
    }


    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
