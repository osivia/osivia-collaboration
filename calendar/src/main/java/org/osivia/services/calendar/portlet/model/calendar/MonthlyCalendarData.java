package org.osivia.services.calendar.portlet.model.calendar;

import java.util.List;

/**
 * Monthly calendar data.
 *
 * @author Cédric Krommenhoek
 * @see CalendarData
 */
public class MonthlyCalendarData extends CalendarData {

    /** Headers. */
    private List<String> headers;
    /** Weeks. */
    private List<MonthlyCalendarWeek> weeks;


    /**
     * Default constructor.
     */
    public MonthlyCalendarData() {
        super();
    }


    /**
     * Getter for headers.
     *
     * @return the headers
     */
    public List<String> getHeaders() {
        return this.headers;
    }

    /**
     * Setter for headers.
     *
     * @param headers the headers to set
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * Getter for weeks.
     *
     * @return the weeks
     */
    public List<MonthlyCalendarWeek> getWeeks() {
        return this.weeks;
    }

    /**
     * Setter for weeks.
     *
     * @param weeks the weeks to set
     */
    public void setWeeks(List<MonthlyCalendarWeek> weeks) {
        this.weeks = weeks;
    }

}
