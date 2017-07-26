package org.osivia.services.forum.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Forum edition form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumEditionForm {

    /** Title. */
    private String title;
    /** Description. */
    private String description;
    /** Vignette. */
    private Vignette vignette;
    /** Message. */
    private String message;
    /** Attachments. */
    private Attachments attachments;

    /** Document type. */
    private String documentType;


    /**
     * Constructor.
     */
    public ForumEditionForm() {
        super();
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
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for vignette.
     *
     * @return the vignette
     */
    public Vignette getVignette() {
        return vignette;
    }

    /**
     * Setter for vignette.
     *
     * @param vignette the vignette to set
     */
    public void setVignette(Vignette vignette) {
        this.vignette = vignette;
    }

    /**
     * Getter for message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for attachments.
     *
     * @return the attachments
     */
    public Attachments getAttachments() {
        return attachments;
    }

    /**
     * Setter for attachments.
     *
     * @param attachments the attachments to set
     */
    public void setAttachments(Attachments attachments) {
        this.attachments = attachments;
    }

    /**
     * Getter for documentType.
     *
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Setter for documentType.
     *
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
