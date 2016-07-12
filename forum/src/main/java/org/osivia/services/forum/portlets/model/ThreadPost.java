package org.osivia.services.forum.portlets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread post view-object.
 *
 * @author Cédric Krommenhoek
 */
public class ThreadPost extends ThreadObject {

    /** Identifier. */
    private String id;
    /** Deletable indicator. */
    private boolean deletable;

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
     * Getter for children.
     * 
     * @return the children
     */
    public List<ThreadPost> getChildren() {
        return this.children;
    }

}
