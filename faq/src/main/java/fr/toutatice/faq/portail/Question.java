package fr.toutatice.faq.portail;


/**
 * FAQ question view-object.
 *
 * @author Cédric Krommenhoek
 */
public class Question {

    /** Identifier. */
    private String id;
    /** Path. */
    private String path;
    /** Title. */
    private String title;
    /** Message. */
    private String message;
    /** Attachments. */
    private String attachments;


    /**
     * Constructor
     */
    public Question() {
        super();
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
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Setter for path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return this.title;
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
     * Getter for attachments.
     * 
     * @return the attachments
     */
    public String getAttachments() {
        return this.attachments;
    }

    /**
     * Setter for attachments.
     * 
     * @param attachments the attachments to set
     */
    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

}
