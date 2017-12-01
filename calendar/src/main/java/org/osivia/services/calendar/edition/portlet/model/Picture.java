package org.osivia.services.calendar.edition.portlet.model;

import org.osivia.services.calendar.common.model.AbstractTemporaryFile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Picture java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Picture extends AbstractTemporaryFile {

    /** Original URL. */
    private String url;
    /** Picture uploaded multipart file. */
    private MultipartFile upload;


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

}
