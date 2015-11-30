package org.osivia.services.forum.portlets.model;

import java.util.Date;

import org.osivia.portal.api.directory.entity.DirectoryPerson;

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
    /** Directory person */
    private DirectoryPerson person;
    /** Profile URL. */
    private String profileURL;
    /** Creation date. */
    private Date date;


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
    public DirectoryPerson getPerson() {
        return this.person;
    }

    /**
     * Setter for person.
     * 
     * @param person the person to set
     */
    public void setPerson(DirectoryPerson person) {
        this.person = person;
    }

    /**
     * Getter for profileURL.
     * 
     * @return the profileURL
     */
    public String getProfileURL() {
        return this.profileURL;
    }

    /**
     * Setter for profileURL.
     * 
     * @param profileURL the profileURL to set
     */
    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Setter for date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
