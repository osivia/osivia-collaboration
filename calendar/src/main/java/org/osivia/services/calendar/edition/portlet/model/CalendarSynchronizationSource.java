package org.osivia.services.calendar.edition.portlet.model;

import org.osivia.services.calendar.common.model.CalendarColor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar synchronization source java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarSynchronizationSource {

    /** Identifier */
    private String id;
    /** URL. */
    private String url;
    /** Color */
    private CalendarColor color;
    /** Display Name */
    private String displayName;


    /**
     * Constructor.
     */
    public CalendarSynchronizationSource() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CalendarSynchronizationSource other = (CalendarSynchronizationSource) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
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

}
