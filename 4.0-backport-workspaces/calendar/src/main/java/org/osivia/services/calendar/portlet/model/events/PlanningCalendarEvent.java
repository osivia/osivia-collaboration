package org.osivia.services.calendar.portlet.model.events;

import java.util.Date;


/**
 * Planning calendar event.
 *
 * @author Cédric Krommenhoek
 * @see Event
 */
public class PlanningCalendarEvent extends DailyEvent {

    /** Header. */
    private PlanningCalendarEventHeader header;


    /**
     * Constructor.
     *
     * @param event event
     * @param currentDate current date
     */
    public PlanningCalendarEvent(Event event, Date currentDate) {
        super(event, currentDate);
    }


    /**
     * Getter for header.
     *
     * @return the header
     */
    public PlanningCalendarEventHeader getHeader() {
        return this.header;
    }

    /**
     * Setter for header.
     *
     * @param header the header to set
     */
    public void setHeader(PlanningCalendarEventHeader header) {
        this.header = header;
    }

}
