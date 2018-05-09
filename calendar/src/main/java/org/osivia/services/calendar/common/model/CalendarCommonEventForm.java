package org.osivia.services.calendar.common.model;

import java.util.Date;

public class CalendarCommonEventForm extends AbstractCalendarEditionForm {

    /** All day indicator. */
    private boolean allDay;
    /** Location. */
    private String location;
    /** Description. */
    private String description;
    /** Attachments. */
    private Attachments attachments;

    /** Start date. */
    private Date startDate;
    /** End date. */
    private Date endDate;

    /** Dates. */
    private CalendarEventDates dates;
	
	public CalendarCommonEventForm() {
		super();
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Attachments getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachments attachments) {
		this.attachments = attachments;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    /**
     * Getter for dates.
     * 
     * @return the dates
     */
    public CalendarEventDates getDates() {
        return dates;
    }

    /**
     * Setter for dates.
     * 
     * @param dates the dates to set
     */
    public void setDates(CalendarEventDates dates) {
        this.dates = dates;
    }
	
	
}
