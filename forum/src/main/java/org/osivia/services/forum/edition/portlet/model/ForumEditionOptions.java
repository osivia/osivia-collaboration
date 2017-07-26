package org.osivia.services.forum.edition.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Forum edition options java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumEditionOptions {

    /** Document type. */
    private DocumentType documentType;
    /** Forum edition mode. */
    private ForumEditionMode mode;
    /** Forum document, may be null. */
    private Document document;
    /** Forum parent path. */
    private String parentPath;

    /** Fragment. */
    private String fragment;
    /** Title. */
    private String title;
    /** View path. */
    private String viewPath;


    /**
     * Constructor.
     */
    public ForumEditionOptions() {
        super();
    }


    /**
     * Getter for documentType.
     *
     * @return the documentType
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * Setter for documentType.
     *
     * @param documentType the documentType to set
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    /**
     * Getter for mode.
     *
     * @return the mode
     */
    public ForumEditionMode getMode() {
        return mode;
    }

    /**
     * Setter for mode.
     *
     * @param mode the mode to set
     */
    public void setMode(ForumEditionMode mode) {
        this.mode = mode;
    }

    /**
     * Getter for document.
     *
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Setter for document.
     *
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Getter for parentPath.
     *
     * @return the parentPath
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * Setter for parentPath.
     *
     * @param parentPath the parentPath to set
     */
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    /**
     * Getter for fragment.
     *
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * Setter for fragment.
     *
     * @param fragment the fragment to set
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
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
     * Getter for viewPath.
     *
     * @return the viewPath
     */
    public String getViewPath() {
        return viewPath;
    }

    /**
     * Setter for viewPath.
     *
     * @param viewPath the viewPath to set
     */
    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

}
