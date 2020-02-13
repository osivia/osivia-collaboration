package org.osivia.services.rss.common.model;

import java.io.File;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Image java-bean.
 * 
 * @author Frédéric Boudan
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Picture {

    /** Vignette original URL. */
    private String url;
    /** Vignette upload multipart file. */
    private MultipartFile upload;
    /** Vignette upload temporary file. */
    private File temporaryFile;
    /** Updated vignette indicator. */
    private boolean updated;
    /** Deleted vignette indicator. */
    private boolean deleted;
    /** Name.*/
    private String name; 

    /**
     * Constructor.
     */
    public Picture() {
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
     * Getter for temporaryFile.
     * 
     * @return the temporaryFile
     */
    public File getTemporaryFile() {
        return temporaryFile;
    }

    /**
     * Setter for temporaryFile.
     * 
     * @param temporaryFile the temporaryFile to set
     */
    public void setTemporaryFile(File temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    /**
     * Getter for updated.
     * 
     * @return the updated
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * Setter for updated.
     * 
     * @param updated the updated to set
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
