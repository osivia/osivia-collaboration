package org.osivia.services.workspace.filebrowser.portlet.model;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * File browser item java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserItem {

    /** Document DTO. */
    private DocumentDTO document;

    /** Title. */
    private String title;
    /** Lock icon. */
    private String lock;
    /** Subscription indicator. */
    private boolean subscription;
    /** Last modification. */
    private Date lastModification;
    /** Last contributor. */
    private String lastContributor;
    /** File size, may be null. */
    private Long size;

    /** Folderish indicator. */
    private boolean folderish;
    /** File MIME type. */
    private String mimeType;
    /** Folderish accepted types. */
    private String acceptedTypes;

    /** Publication infos. */
    private NuxeoPublicationInfos publicationInfos;
    /** Permissions. */
    private NuxeoPermissions permissions;


    /**
     * Constructor.
     */
    public FileBrowserItem() {
        super();
    }


    /**
     * Getter for document.
     * 
     * @return the document
     */
    public DocumentDTO getDocument() {
        return document;
    }

    /**
     * Setter for document.
     * 
     * @param document the document to set
     */
    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for lock.
     * 
     * @return the lock
     */
    public String getLock() {
        return lock;
    }

    /**
     * Setter for lock.
     * 
     * @param lock the lock to set
     */
    public void setLock(String lock) {
        this.lock = lock;
    }

    /**
     * Getter for subscription.
     * 
     * @return the subscription
     */
    public boolean isSubscription() {
        return subscription;
    }

    /**
     * Setter for subscription.
     * 
     * @param subscription the subscription to set
     */
    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    /**
     * Getter for lastModification.
     * 
     * @return the lastModification
     */
    public Date getLastModification() {
        return lastModification;
    }

    /**
     * Setter for lastModification.
     * 
     * @param lastModification the lastModification to set
     */
    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    /**
     * Getter for lastContributor.
     * 
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }

    /**
     * Setter for lastContributor.
     * 
     * @param lastContributor the lastContributor to set
     */
    public void setLastContributor(String lastContributor) {
        this.lastContributor = lastContributor;
    }

    /**
     * Getter for size.
     * 
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Setter for size.
     * 
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Getter for folderish.
     * 
     * @return the folderish
     */
    public boolean isFolderish() {
        return folderish;
    }

    /**
     * Setter for folderish.
     * 
     * @param folderish the folderish to set
     */
    public void setFolderish(boolean folderish) {
        this.folderish = folderish;
    }

    /**
     * Getter for mimeType.
     * 
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Setter for mimeType.
     * 
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Getter for acceptedTypes.
     * 
     * @return the acceptedTypes
     */
    public String getAcceptedTypes() {
        return acceptedTypes;
    }

    /**
     * Setter for acceptedTypes.
     * 
     * @param acceptedTypes the acceptedTypes to set
     */
    public void setAcceptedTypes(String acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    /**
     * Getter for publicationInfos.
     * 
     * @return the publicationInfos
     */
    public NuxeoPublicationInfos getPublicationInfos() {
        return publicationInfos;
    }

    /**
     * Setter for publicationInfos.
     * 
     * @param publicationInfos the publicationInfos to set
     */
    public void setPublicationInfos(NuxeoPublicationInfos publicationInfos) {
        this.publicationInfos = publicationInfos;
    }

    /**
     * Getter for permissions.
     * 
     * @return the permissions
     */
    public NuxeoPermissions getPermissions() {
        return permissions;
    }

    /**
     * Setter for permissions.
     * 
     * @param permissions the permissions to set
     */
    public void setPermissions(NuxeoPermissions permissions) {
        this.permissions = permissions;
    }

}
