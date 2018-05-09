package org.osivia.services.calendar.synchronization.edition.portlet.model;

import org.osivia.services.calendar.common.model.CalendarColor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar synchronization source edition form java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarSynchronizationEditionForm {

    /** Synchronization source URL. */
    private String url;
    /** Synchronization source color. */
    private CalendarColor color;
    /** Synchronization source display name. */
    private String displayName;

    /** Synchronization source identifier, null in case of creation. */
    private String sourceId;
    /** Done indicator. */
    private boolean done;


    /**
     * Constructor.
     */
    public CalendarSynchronizationEditionForm() {
        super();
    }


    /**
     * Getter for url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     * 
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for color.
     * 
     * @return the color
     */
    public CalendarColor getColor() {
        return color;
    }

    /**
     * Setter for color.
     * 
     * @param color the color to set
     */
    public void setColor(CalendarColor color) {
        this.color = color;
    }

    /**
     * Setter for color.
     * 
     * @param color the color to set
     */
    public void setColor(String colorId) {
    	this.color = CalendarColor.fromId(colorId);
    }
    
    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for sourceId.
     * 
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Setter for sourceId.
     * 
     * @param sourceId the sourceId to set
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

}
