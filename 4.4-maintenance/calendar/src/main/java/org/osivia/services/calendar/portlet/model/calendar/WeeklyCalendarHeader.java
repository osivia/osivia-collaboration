package org.osivia.services.calendar.portlet.model.calendar;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.osivia.services.calendar.portlet.service.ICalendarService;

/**
 * Weekly calendar header.
 *
 * @author Cédric Krommenhoek
 */
public class WeeklyCalendarHeader {

    /** Day name. */
    private String name;

    /** Today indicator. */
    private final boolean today;
    /** Date parameter. */
    private final String dateParameter;


    /**
     * Constructor.
     *
     * @param date date
     */
    public WeeklyCalendarHeader(Date date) {
        super();
        this.today = DateUtils.isSameDay(date, new Date());
        this.dateParameter = StringEscapeUtils.escapeHtml(ICalendarService.SELECTED_DATE_FORMAT.format(date));
    }


    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for today.
     * 
     * @return the today
     */
    public boolean isToday() {
        return this.today;
    }

    /**
     * Getter for dateParameter.
     * 
     * @return the dateParameter
     */
    public String getDateParameter() {
        return this.dateParameter;
    }

}
