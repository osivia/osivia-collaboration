package org.osivia.services.calendar.portlet.model.calendar;

import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateUtils;
import org.osivia.services.calendar.portlet.service.ICalendarService;

/**
 * Monthly calendar day representation.
 *
 * @author Cédric Krommenhoek
 */
public class MonthlyCalendarDay {

    /** Day name. */
    private String name;
    /** Month display. */
    private String monthDisplay;
    /** Day in current month indicator. */
    private boolean currentMonth;

    /** Today indicator. */
    private final boolean today;
    /** Date parameter. */
    private final String dateParameter;


    /**
     * Default constructor.
     *
     * @param date date
     */
    public MonthlyCalendarDay(Date date) {
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
     * Getter for monthDisplay.
     *
     * @return the monthDisplay
     */
    public String getMonthDisplay() {
        return this.monthDisplay;
    }

    /**
     * Setter for monthDisplay.
     *
     * @param monthDisplay the monthDisplay to set
     */
    public void setMonthDisplay(String monthDisplay) {
        this.monthDisplay = monthDisplay;
    }

    /**
     * Getter for currentMonth.
     *
     * @return the currentMonth
     */
    public boolean isCurrentMonth() {
        return this.currentMonth;
    }

    /**
     * Setter for currentMonth.
     *
     * @param currentMonth the currentMonth to set
     */
    public void setCurrentMonth(boolean currentMonth) {
        this.currentMonth = currentMonth;
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
