package org.osivia.services.forum.thread.portlet.model;

import org.apache.commons.beanutils.BeanUtils;
import org.osivia.services.forum.util.model.ForumFiles;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Forum thread object java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Cloneable
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumThreadObject implements Cloneable {

    /** Identifier. */
    private String id;
    /** User name. */
    private String user;
    /** Date. */
    private Date date;
    /** Modified date. */
    private Date modified;
    /** Message. */
    private String message;
    /** Attachments. */
    private ForumFiles attachments;
    /** Editable indicator. */
    private boolean editable;


    /**
     * Constructor.
     */
    public ForumThreadObject() {
        super();
    }


    @Override
    public ForumThreadObject clone() {
        ForumThreadObject clone = new ForumThreadObject();
        try {
            BeanUtils.copyProperties(clone, this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        clone.setAttachments(this.getAttachments().clone());
        return clone;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
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
     * Getter for user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Setter for user.
     *
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Getter for date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for modified.
     *
     * @return the modified
     */
    public Date getModified() {
        return modified;
    }

    /**
     * Setter for modified.
     *
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
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

    /**
     * Getter for editable.
     *
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for editable.
     *
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
