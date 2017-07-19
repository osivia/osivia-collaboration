package org.osivia.services.editor.link.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Editor link form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorLinkForm {

    /** Done indicator. */
    private boolean done;
    /** URL. */
    private String url;
    /** URL type. */
    private UrlType urlType;
    /** Manual URL. */
    private String manualUrl;
    /** Document webId. */
    private String documentWebId;
    /** Text. */
    private String text;
    /** Title. */
    private String title;
    /** Display text indicator. */
    private boolean displayText;
    /** Document DTO. */
    private DocumentDTO document;


    /**
     * Constructor.
     */
    public EditorLinkForm() {
        super();
    }


    /**
     * Getter for done.
     * 
     * @return the done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Setter for done.
     * 
     * @param done the done to set
     */
    public void setDone(boolean done) {
        this.done = done;
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
     * Getter for urlType.
     *
     * @return the urlType
     */
    public UrlType getUrlType() {
        return urlType;
    }

    /**
     * Setter for urlType.
     *
     * @param urlType the urlType to set
     */
    public void setUrlType(UrlType urlType) {
        this.urlType = urlType;
    }

    /**
     * Getter for manualUrl.
     *
     * @return the manualUrl
     */
    public String getManualUrl() {
        return manualUrl;
    }

    /**
     * Setter for manualUrl.
     *
     * @param manualUrl the manualUrl to set
     */
    public void setManualUrl(String manualUrl) {
        this.manualUrl = manualUrl;
    }

    /**
     * Getter for documentWebId.
     *
     * @return the documentWebId
     */
    public String getDocumentWebId() {
        return documentWebId;
    }

    /**
     * Setter for documentWebId.
     *
     * @param documentWebId the documentWebId to set
     */
    public void setDocumentWebId(String documentWebId) {
        this.documentWebId = documentWebId;
    }

    /**
     * Getter for text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for text.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
     * Getter for displayText.
     *
     * @return the displayText
     */
    public boolean isDisplayText() {
        return displayText;
    }

    /**
     * Setter for displayText.
     *
     * @param displayText the displayText to set
     */
    public void setDisplayText(boolean displayText) {
        this.displayText = displayText;
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

}
