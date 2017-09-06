package org.osivia.services.forum.thread.portlet.model;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Forum thread form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class ForumThreadForm {

    /** Forum thread. */
    private ForumThreadObject thread;
    /** Forum thread posts. */
    private ForumThreadPosts posts;
    /** Forum thread reply. */
    private ForumThreadObject reply;

    /** Commentable indicator. */
    private boolean commentable;


    /**
     * Constructor.
     */
    public ForumThreadForm() {
        super();
    }


    /**
     * Getter for thread.
     *
     * @return the thread
     */
    public ForumThreadObject getThread() {
        return thread;
    }

    /**
     * Setter for thread.
     *
     * @param thread the thread to set
     */
    public void setThread(ForumThreadObject thread) {
        this.thread = thread;
    }

    /**
     * Getter for posts.
     *
     * @return the posts
     */
    public ForumThreadPosts getPosts() {
        return posts;
    }

    /**
     * Setter for posts.
     *
     * @param posts the posts to set
     */
    public void setPosts(ForumThreadPosts posts) {
        this.posts = posts;
    }

    /**
     * Getter for reply.
     *
     * @return the reply
     */
    public ForumThreadObject getReply() {
        return reply;
    }

    /**
     * Setter for reply.
     *
     * @param reply the reply to set
     */
    public void setReply(ForumThreadObject reply) {
        this.reply = reply;
    }

    /**
     * Getter for commentable.
     *
     * @return the commentable
     */
    public boolean isCommentable() {
        return commentable;
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
