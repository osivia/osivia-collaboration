package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Date;

/**
 * Event.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 */
public class Event {

    /** Event time. */
    private String time;

    /** Event identifier. */
    private final String id;
    /** Event title. */
    private final String title;
    /** Event start date. */
    private final Date startDate;
    /** Event end date. */
    private final Date endDate;
    /** Event all day indicator. */
    private final boolean allDay;
    /** Event view document URL. */
    private final String viewURL;
    /** Background color */
    private final String bckgcolor;
    /** Summary */
    private final String summary;
    /** Id event source */
    private final String idEventSource;
    /** Id parent source */
    private final String idParentSource;

    /**
     * Constructor.
     *
     * @param id event identifier
     * @param title event title
     * @param startDate event start date
     * @param endDate event end date
     * @param viewURL event view document URL
     */
    public Event(String id, String title, Date startDate, Date endDate, boolean allDay, String bckgcolor, String viewURL, String idEventSrc, String idParentSrc) {
        super();
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allDay = allDay;
        this.bckgcolor = bckgcolor;
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
        this(event.id, event.title, event.startDate, event.endDate, event.allDay, event.bckgcolor, event.viewURL, event.idEventSource, event.idParentSource);
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

    
    /**
     * Getter for background Color
     */
    public String getBckgColor() {
		return bckgcolor;
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
