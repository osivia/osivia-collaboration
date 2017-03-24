package org.osivia.services.editor.link.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Editor link form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorLinkForm {

    /** Done indicator. */
    private boolean done;
    /** Link value. */
    private String link;


    /**
     * Constructor.
     */
    public EditorLinkForm() {
        super();
    }


    /**
     * Getter for done.
     * 
     * @return the done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Setter for done.
     * 
     * @param done the done to set
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Getter for link.
     * 
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Setter for link.
     * 
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

}
