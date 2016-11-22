package org.osivia.services.calendar.portlet.model.events;

import java.util.Date;

/**
 * Planning calendar events data.
 *
 * @author Cédric Krommenhoek
 * @see MappedEventsData
 * @see PlanningCalendarEventHeader
 * @see DailyEvent
 */
public class PlanningCalendarEventsData extends MappedEventsData<PlanningCalendarEventHeader, DailyEvent> {

    /** Last date. */
    private Date lastDate;


    /**
     * Constructor.
     */
    public PlanningCalendarEventsData() {
        super();
    }


    /**
     * Getter for lastDate.
     * 
     * @return the lastDate
     */
    public Date getLastDate() {
        return lastDate;
    }

    /**
     * Setter for lastDate.
     * 
     * @param lastDate the lastDate to set
     */
    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

}
