package org.osivia.services.forum.portlets.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Forum thread post reply form.
 * 
 * @author Cédric Krommenhoek
 */
public class ThreadPostReplyForm {

    /** Thread post content. */
    private String content;
    /** Thread post attachment. */
    private MultipartFile attachment;


    /**
     * Default constructor.
     */
    public ThreadPostReplyForm() {
        super();
    }


    /**
     * Getter for content.
     *
     * @return the content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Setter for content.
     *
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Getter for attachment.
     *
     * @return the attachment
     */
    public MultipartFile getAttachment() {
        return this.attachment;
    }

    /**
     * Setter for attachment.
     *
     * @param attachment the attachment to set
     */
    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

}
