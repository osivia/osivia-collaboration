package org.osivia.services.forum.portlets.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Thread post view-object.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThreadPost extends ThreadObject {

    /** Identifier. */
    private String id;
    /** Deletable indicator. */
    private boolean deletable;
    /** Attachment name. */
    private String attachmentName;
    /** Attachment URL. */
    private String attachmentURL;

    /** Thread post children. */
    private final List<ThreadPost> children;


    /**
     * Constructor.
     */
    public ThreadPost() {
        super();
        this.children = new ArrayList<ThreadPost>();
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for deletable.
     *
     * @return the deletable
     */
    public boolean isDeletable() {
        return this.deletable;
    }

    /**
     * Setter for deletable.
     *
     * @param deletable the deletable to set
     */
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * Getter for attachmentName.
     * 
     * @return the attachmentName
     */
    public String getAttachmentName() {
        return this.attachmentName;
    }

    /**
     * Setter for attachmentName.
     * 
     * @param attachmentName the attachmentName to set
     */
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    /**
     * Getter for attachmentURL.
     * 
     * @return the attachmentURL
     */
    public String getAttachmentURL() {
        return this.attachmentURL;
    }

    /**
     * Setter for attachmentURL.
     * 
     * @param attachmentURL the attachmentURL to set
     */
    public void setAttachmentURL(String attachmentURL) {
        this.attachmentURL = attachmentURL;
    }

    /**
     * Getter for children.
     * 
     * @return the children
     */
    public List<ThreadPost> getChildren() {
        return this.children;
    }

}
