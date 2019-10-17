package org.osivia.services.workspace.filebrowser.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * File browser item java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserItem {

    /**
     * Document DTO.
     */
    private DocumentDTO document;
    /**
     * Parent document DTO.
     */
    private DocumentDTO parentDocument;
    /**
     * Native order.
     */
    private int nativeOrder;

    /**
     * Title.
     */
    private String title;
    /**
     * Lock icon.
     */
    private String lock;
    /**
     * Subscription indicator.
     */
    private boolean subscription;
    /**
     * Last modification.
     */
    private Date lastModification;
    /**
     * Last contributor.
     */
    private String lastContributor;
    /**
     * File size, may be null.
     */
    private Long size;

    /**
     * Folderish indicator.
     */
    private boolean folderish;
    /**
     * File MIME type.
     */
    private String mimeType;
    /**
     * Folderish accepted types.
     */
    private String acceptedTypes;

    /**
     * Publication infos.
     */
    private NuxeoPublicationInfos publicationInfos;
    /**
     * Permissions.
     */
    private NuxeoPermissions permissions;


    /**
     * Constructor.
     */
    public FileBrowserItem() {
        super();
    }


    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public DocumentDTO getParentDocument() {
        return parentDocument;
    }

    public void setParentDocument(DocumentDTO parentDocument) {
        this.parentDocument = parentDocument;
    }

    public int getNativeOrder() {
        return nativeOrder;
    }

    public void setNativeOrder(int nativeOrder) {
        this.nativeOrder = nativeOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isSubscription() {
        return subscription;
    }

    public void setSubscription(boolean subscription) {
        this.subscription = subscription;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    public String getLastContributor() {
        return lastContributor;
    }

    public void setLastContributor(String lastContributor) {
        this.lastContributor = lastContributor;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public boolean isFolderish() {
        return folderish;
    }

    public void setFolderish(boolean folderish) {
        this.folderish = folderish;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(String acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public NuxeoPublicationInfos getPublicationInfos() {
        return publicationInfos;
    }

    public void setPublicationInfos(NuxeoPublicationInfos publicationInfos) {
        this.publicationInfos = publicationInfos;
    }

    public NuxeoPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(NuxeoPermissions permissions) {
        this.permissions = permissions;
    }
}
