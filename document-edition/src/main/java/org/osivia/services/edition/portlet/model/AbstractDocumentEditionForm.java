package org.osivia.services.edition.portlet.model;

import org.osivia.services.edition.portlet.service.DocumentEditionService;

/**
 * Document edition form abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractDocumentEditionForm {

    /**
     * Upload max size.
     */
    private final long uploadMaxSize;


    /**
     * Window properties.
     */
    private DocumentEditionWindowProperties windowProperties;
    /**
     * Name.
     */
    private String name;
    /**
     * Document path.
     */
    private String path;
    /**
     * Document creation indicator.
     */
    private boolean creation;
    /**
     * Fullscreen indicator.
     */
    private boolean fullscreen;

    /**
     * Title.
     */
    private String title;
    /**
     * Original title.
     */
    private String originalTitle;
    /**
     * Attachments.
     */
    private Attachments attachments;
    /**
     * Metadata.
     */
    private DocumentEditionMetadata metadata;

    /**
     * Extract archive indicator.
     */
    private Boolean extractArchive;

    /**
     * Remote user (for logging in validation)
     */
    private String remoteUser;


    /**
     * Constructor.
     */
    protected AbstractDocumentEditionForm() {
        super();
        this.uploadMaxSize = DocumentEditionService.MAX_UPLOAD_SIZE;
    }


    public long getUploadMaxSize() {
        return uploadMaxSize;
    }

    public DocumentEditionWindowProperties getWindowProperties() {
        return windowProperties;
    }

    public void setWindowProperties(DocumentEditionWindowProperties windowProperties) {
        this.windowProperties = windowProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCreation() {
        return creation;
    }

    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Attachments getAttachments() {
        return attachments;
    }

    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }

    public DocumentEditionMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DocumentEditionMetadata metadata) {
        this.metadata = metadata;
    }

    public Boolean getExtractArchive() {
        return extractArchive;
    }

    public void setExtractArchive(Boolean extractArchive) {
        this.extractArchive = extractArchive;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }
}
