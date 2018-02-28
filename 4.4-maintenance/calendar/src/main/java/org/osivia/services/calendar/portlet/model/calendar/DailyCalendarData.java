package org.osivia.services.calendar.portlet.model.calendar;

/**
 * Daily calendar data.
 *
 * @author Cédric Krommenhoek
 * @see ScrollableCalendarData
 */
public class DailyCalendarData extends ScrollableCalendarData {

    /** Header. */
    private String header;


    /**
     * Default constructor.
     */
    public DailyCalendarData() {
        super();
    }


    /**
     * Getter for header.
     *
     * @return the header
     */
    public String getHeader() {
        return this.header;
    }

    /**
     * Setter for header.
     *
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

}
