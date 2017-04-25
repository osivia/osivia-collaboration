package org.osivia.services.workspace.common.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Task creation form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskCreationForm {

    /** Title. */
    private String title;
    /** Description. */
    private String description;
    /** Type. */
    private String type;
    /** Valid indicator. */
    private boolean valid;


    /**
     * Constructor.
     */
    public TaskCreationForm() {
        super();
    }


    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
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
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for valid.
     * 
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Setter for valid.
     * 
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
