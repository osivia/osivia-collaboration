package org.osivia.services.forum.portlets.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osivia.portal.api.directory.v2.model.Person;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentAttachmentDTO;

/**
 * Thread object abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class ThreadObject {

    /** Message. */
    private String message;
    /** Author. */
    private String author;
    /** Person */
    private Person person;
    /** Profile URL. */
    private String profileUrl;
    /** Creation date. */
    private Date date;
    /** Attachments. */
    private List<DocumentAttachmentDTO> attachments = new ArrayList<DocumentAttachmentDTO>();;

    /**
     * Constructor.
     */
    public ThreadObject() {
        super();
    }


    /**
     * Getter for message.
     * 
     * @return the message
     */
    public String getMessage() {
        return this.message;
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
     * Getter for author.
     * 
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Setter for author.
     * 
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Getter for person.
     * 
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Setter for person.
     * 
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Getter for profileUrl.
     * 
     * @return the profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * Setter for profileUrl.
     * 
     * @param profileUrl the profileUrl to set
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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
     * Getter for attachments.
     * 
     * @return the attachments
     */
    public List<DocumentAttachmentDTO> getAttachments() {
        return this.attachments;
    }

    /**
     * Setter for attachments.
     * 
     * @param attachments the attachments to set
     */
    public void setAttachments(List<DocumentAttachmentDTO> attachments) {
        this.attachments = attachments;
    }    

}
