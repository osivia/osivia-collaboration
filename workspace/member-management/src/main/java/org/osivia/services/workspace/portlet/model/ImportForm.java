package org.osivia.services.workspace.portlet.model;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Import form java-bean
 *
 * @author Loïc Billon
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class ImportForm {

    /**
     * Role.
     */
    private WorkspaceRole role;

    /**
     * Local groups.
     */
    private List<CollabProfile> localGroups;

    /**
     * Message.
     */
    private String message;
    /**
     * Upload max size.
     */
    private long uploadMaxSize;
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
     * Batch initiator
     */
    private String initiator;

    private boolean loaded;

    /**
     * Message if a batch is running
     */
    private boolean batchRunning;

    /**
     * Time estimated
     */
    private int timeEstimated;


    public WorkspaceRole getRole() {
        return role;
    }

    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    public List<CollabProfile> getLocalGroups() {
        return localGroups;
    }

    public void setLocalGroups(List<CollabProfile> localGroups) {
        this.localGroups = localGroups;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUploadMaxSize() {
        return uploadMaxSize;
    }

    public void setUploadMaxSize(long uploadMaxSize) {
        this.uploadMaxSize = uploadMaxSize;
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

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isBatchRunning() {
        return batchRunning;
    }

    public void setBatchRunning(boolean batchRunning) {
        this.batchRunning = batchRunning;
    }

    public int getTimeEstimated() {
        return timeEstimated;
    }

    public void setTimeEstimated(int timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

}
