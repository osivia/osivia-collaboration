package org.osivia.services.forum.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Forum thread view object.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
