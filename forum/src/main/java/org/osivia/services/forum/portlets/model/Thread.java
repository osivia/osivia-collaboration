package org.osivia.services.forum.portlets.model;


/**
 * Forum thread view object.
 *
 * @author Cédric Krommenhoek
 */
public class Thread extends ThreadObject {

    /** Commentable indicator. */
    private boolean commentable;


    /**
     * Constructor.
     */
    public Thread() {
        super();
    }


    /**
     * Getter for commentable.
     *
     * @return the commentable
     */
    public boolean isCommentable() {
        return this.commentable;
    }

    /**
     * Setter for commentable.
     *
     * @param commentable the commentable to set
     */
    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

}
