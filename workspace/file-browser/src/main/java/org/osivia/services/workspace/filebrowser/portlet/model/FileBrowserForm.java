package org.osivia.services.workspace.filebrowser.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * File browser form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class FileBrowserForm {

    /** File browser items. */
    private List<FileBrowserItem> items;
    /** File browser sort criteria. */
    private FileBrowserSortCriteria criteria;
    /** Uploadable indicator. */
    private boolean uploadable;
    /** Upload multipart files. */
    private List<MultipartFile> upload;
    /** Upload max file size. */
    private Long maxFileSize;

    /** Initialized indicator. */
    private boolean initialized;


    /**
     * Constructor.
     */
    public FileBrowserForm() {
        super();
    }


    /**
     * Getter for items.
     * 
     * @return the items
     */
    public List<FileBrowserItem> getItems() {
        return items;
    }

    /**
     * Setter for items.
     * 
     * @param items the items to set
     */
    public void setItems(List<FileBrowserItem> items) {
        this.items = items;
    }

    /**
     * Getter for criteria.
     * 
     * @return the criteria
     */
    public FileBrowserSortCriteria getCriteria() {
        return criteria;
    }

    /**
     * Setter for criteria.
     * 
     * @param criteria the criteria to set
     */
    public void setCriteria(FileBrowserSortCriteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Getter for uploadable.
     * 
     * @return the uploadable
     */
    public boolean isUploadable() {
        return uploadable;
    }

    /**
     * Setter for uploadable.
     * 
     * @param uploadable the uploadable to set
     */
    public void setUploadable(boolean uploadable) {
        this.uploadable = uploadable;
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

    /**
     * Getter for maxFileSize.
     * 
     * @return the maxFileSize
     */
    public Long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Setter for maxFileSize.
     * 
     * @param maxFileSize the maxFileSize to set
     */
    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * Getter for initialized.
     * 
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Setter for initialized.
     * 
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
