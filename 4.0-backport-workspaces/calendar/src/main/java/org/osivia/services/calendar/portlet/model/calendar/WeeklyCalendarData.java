package org.osivia.services.calendar.portlet.model.calendar;

import java.util.List;

/**
 * Weekly calendar data.
 *
 * @author Cédric Krommenhoek
 * @see ScrollableCalendarData
 */
public class WeeklyCalendarData extends ScrollableCalendarData {

    /** Headers. */
    private List<WeeklyCalendarHeader> headers;
    /** Column week day. */
    private int today;


    /**
     * Default constructor.
     */
    public WeeklyCalendarData() {
        super();
    }


    /**
     * Getter for headers.
     *
     * @return the headers
     */
    public List<WeeklyCalendarHeader> getHeaders() {
        return this.headers;
    }

    /**
     * Setter for headers.
     *
     * @param headers the headers to set
     */
    public void setHeaders(List<WeeklyCalendarHeader> headers) {
        this.headers = headers;
    }

    /**
     * Getter for today.
     * 
     * @return the today
     */
    public int getToday() {
        return this.today;
    }

    /**
     * Setter for today.
     * 
     * @param today the today to set
     */
    public void setToday(int today) {
        this.today = today;
    }

}
