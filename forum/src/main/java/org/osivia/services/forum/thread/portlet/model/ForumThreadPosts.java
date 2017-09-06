package org.osivia.services.forum.thread.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Forum thread posts java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumThreadPosts {

    /** Forum thread posts list. */
    private List<ForumThreadObject> list;
    /** Edited forum thread post. */
    private ForumThreadObject editedPost;
    /** Edited forum thread post identifier. */
    private String editedId;
    /** Deleted forum thread post identifier. */
    private String deletedId;


    /**
     * Constructor.
     */
    public ForumThreadPosts() {
        super();
    }


    /**
     * Getter for list.
     *
     * @return the list
     */
    public List<ForumThreadObject> getList() {
        return list;
    }

    /**
     * Setter for list.
     *
     * @param list the list to set
     */
    public void setList(List<ForumThreadObject> list) {
        this.list = list;
    }

    /**
     * Getter for editedPost.
     *
     * @return the editedPost
     */
    public ForumThreadObject getEditedPost() {
        return editedPost;
    }

    /**
     * Setter for editedPost.
     *
     * @param editedPost the editedPost to set
     */
    public void setEditedPost(ForumThreadObject editedPost) {
        this.editedPost = editedPost;
    }

    /**
     * Getter for editedId.
     *
     * @return the editedId
     */
    public String getEditedId() {
        return editedId;
    }

    /**
     * Setter for editedId.
     *
     * @param editedId the editedId to set
     */
    public void setEditedId(String editedId) {
        this.editedId = editedId;
    }

    /**
     * Getter for deletedId.
     *
     * @return the deletedId
     */
    public String getDeletedId() {
        return deletedId;
    }

    /**
     * Setter for deletedId.
     *
     * @param deletedId the deletedId to set
     */
    public void setDeletedId(String deletedId) {
        this.deletedId = deletedId;
    }

}
