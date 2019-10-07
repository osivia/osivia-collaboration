package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Event.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Event {

    /**
     * Event identifier.
     */
    private final String id;
    /**
     * Event title.
     */
    private final String title;
    /**
     * Event start date.
     */
    private final Date startDate;
    /**
     * Event end date.
     */
    private final Date endDate;
    /**
     * Event all day indicator.
     */
    private final boolean allDay;
    /**
     * Event view document URL.
     */
    private final String viewURL;
    /**
     * Summary
     */
    private final String summary;
    /**
     * Id event source
     */
    private final String idEventSource;
    /**
     * Id parent source
     */
    private final String idParentSource;
    /**
     * Event time.
     */
    private String time;
    /**
     * Event last modified date.
     */
    private Date lastModified;
    /**
     * Event location.
     */
    private String location;
    /**
     * Event description.
     */
    private String description;
    /**
     * Background color
     */
    private String bckgColor;
    /**
     * Event preview URL.
     */
    private String previewUrl;


    /**
     * Constructor.
     *
     * @param id        event identifier
     * @param title     event title
     * @param startDate event start date
     * @param endDate   event end date
     * @param viewURL   event view document URL
     */
    public Event(String id, String title, Date startDate, Date endDate, boolean allDay, String bckgColor, String viewURL, String idEventSrc, String idParentSrc) {
        super();
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.bckgColor = bckgColor;
        this.summary = null;
        this.viewURL = viewURL;
        this.idEventSource = idEventSrc;
        this.idParentSource = idParentSrc;
    }

    /**
     * Constructor.
     *
     * @param event event
     */
    public Event(Event event) {
        this(event.id, event.title, event.startDate, event.endDate, event.allDay, event.bckgColor, event.viewURL, event.idEventSource, event.idParentSource);
        this.previewUrl = event.previewUrl;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Event [title=" + this.title + ", time=" + this.time + "]";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Event other = (Event) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for time.
     *
     * @return the time
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Setter for time.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Getter for lastModified.
     *
     * @return the lastModified
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * Setter for lastModified.
     *
     * @param lastModified the lastModified to set
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Getter for location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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
     * Getter for bckgColor.
     *
     * @return the bckgColor
     */
    public String getBckgColor() {
        return bckgColor;
    }

    /**
     * Setter for bckgColor.
     *
     * @param bckgColor the bckgColor to set
     */
    public void setBckgColor(String bckgColor) {
        this.bckgColor = bckgColor;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
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
     * Getter for title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter for startDate.
     *
     * @return the startDate
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Getter for endDate.
     *
     * @return the endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }


    public String getSummary() {
        return summary;
    }

    /**
     * Getter for allDay.
     *
     * @return the allDay
     */
    public boolean isAllDay() {
        return this.allDay;
    }

    /**
     * Getter for viewURL.
     *
     * @return the viewURL
     */
    public String getViewURL() {
        return this.viewURL;
    }

    /**
     * Getter for idEvent source
     *
     * @return idEventSource
     */
    public String getIdEventSource() {
        return idEventSource;
    }

    /**
     * Getter for idParentSource
     *
     * @return the idParentSource
     */
    public String getIdParentSource() {
        return idParentSource;
    }


}
