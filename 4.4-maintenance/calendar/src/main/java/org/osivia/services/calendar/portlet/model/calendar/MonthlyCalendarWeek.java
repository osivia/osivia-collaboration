package org.osivia.services.calendar.portlet.model.calendar;

import java.util.List;

/**
 * Monthly calendar week representation.
 *
 * @author Cédric Krommenhoek
 */
public class MonthlyCalendarWeek {

    /** Week name. */
    private String name;
    /** Week days. */
    private List<MonthlyCalendarDay> days;


    /**
     * Default constructor.
     */
    public MonthlyCalendarWeek() {
        super();
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
     * Getter for days.
     *
     * @return the days
     */
    public List<MonthlyCalendarDay> getDays() {
        return this.days;
    }

    /**
     * Setter for days.
     *
     * @param days the days to set
     */
    public void setDays(List<MonthlyCalendarDay> days) {
        this.days = days;
    }

}
