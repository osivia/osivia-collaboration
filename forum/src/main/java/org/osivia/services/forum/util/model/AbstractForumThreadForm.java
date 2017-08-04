package org.osivia.services.forum.util.model;

/**
 * Forum thread post abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractForumThreadForm {

    /** Message. */
    private String message;
    /** Attachments. */
    private ForumFiles attachments;


    /**
     * Constructor.
     */
    public AbstractForumThreadForm() {
        super();
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
    public ForumFiles getAttachments() {
        return attachments;
    }

    /**
     * Setter for attachments.
     *
     * @param attachments the attachments to set
     */
    public void setAttachments(ForumFiles attachments) {
        this.attachments = attachments;
    }

}
