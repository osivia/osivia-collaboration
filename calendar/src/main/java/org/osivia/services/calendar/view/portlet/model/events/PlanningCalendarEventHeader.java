package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Date;

/**
 * Planning calendar event header.
 *
 * @author Cédric Krommenhoek
 */
public class PlanningCalendarEventHeader implements Comparable<PlanningCalendarEventHeader> {

    /** Day of month. */
    private String dayOfMonth;
    /** Day of week. */
    private String dayOfWeek;
    /** Month. */
    private String month;

    /** Date object, required for sorting. */
    private final Date date;


    /**
     * Default constructor.
     *
     * @param date date
     */
    public PlanningCalendarEventHeader(Date date) {
        super();
        this.date = date;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(PlanningCalendarEventHeader other) {
        int result;
        if (this.equals(other)) {
            result = 0;
        } else {
            result = this.date.compareTo(other.date);
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.dayOfMonth == null) ? 0 : this.dayOfMonth.hashCode());
        result = (prime * result) + ((this.month == null) ? 0 : this.month.hashCode());
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
        PlanningCalendarEventHeader other = (PlanningCalendarEventHeader) obj;
        if (this.dayOfMonth == null) {
            if (other.dayOfMonth != null) {
                return false;
            }
        } else if (!this.dayOfMonth.equals(other.dayOfMonth)) {
            return false;
        }
        if (this.month == null) {
            if (other.month != null) {
                return false;
            }
        } else if (!this.month.equals(other.month)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for dayOfMonth.
     *
     * @return the dayOfMonth
     */
    public String getDayOfMonth() {
        return this.dayOfMonth;
    }

    /**
     * Setter for dayOfMonth.
     *
     * @param dayOfMonth the dayOfMonth to set
     */
    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * Getter for dayOfWeek.
     *
     * @return the dayOfWeek
     */
    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    /**
     * Setter for dayOfWeek.
     *
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Getter for month.
     *
     * @return the month
     */
    public String getMonth() {
        return this.month;
    }

    /**
     * Setter for month.
     *
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

}
