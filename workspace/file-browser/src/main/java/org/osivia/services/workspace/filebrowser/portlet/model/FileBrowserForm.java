package org.osivia.services.workspace.filebrowser.portlet.model;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * File browser form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class FileBrowserForm {

    /**
     * File browser items.
     */
    private List<FileBrowserItem> items;
    /**
     * File browser sort criteria.
     */
    private FileBrowserSortCriteria criteria;
    /**
     * Uploadable indicator.
     */
    private boolean uploadable;
    /**
     * Upload multipart files.
     */
    private List<MultipartFile> upload;
    /**
     * Upload max file size.
     */
    private Long maxFileSize;

    /**
     * Document base path.
     */
    private String basePath;
    /**
     * Document path.
     */
    private String path;
    /**
     * List mode indicator.
     */
    private boolean listMode;
    /**
     * Initialized indicator.
     */
    private boolean initialized;


    /**
     * Constructor.
     */
    public FileBrowserForm() {
        super();
    }


    public List<FileBrowserItem> getItems() {
        return items;
    }

    public void setItems(List<FileBrowserItem> items) {
        this.items = items;
    }

    public FileBrowserSortCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(FileBrowserSortCriteria criteria) {
        this.criteria = criteria;
    }

    public boolean isUploadable() {
        return uploadable;
    }

    public void setUploadable(boolean uploadable) {
        this.uploadable = uploadable;
    }

    public List<MultipartFile> getUpload() {
        return upload;
    }

    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isListMode() {
        return listMode;
    }

    public void setListMode(boolean listMode) {
        this.listMode = listMode;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
