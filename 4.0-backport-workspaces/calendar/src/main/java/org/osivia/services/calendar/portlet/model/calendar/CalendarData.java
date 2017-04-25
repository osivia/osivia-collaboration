package org.osivia.services.calendar.portlet.model.calendar;

import java.util.Date;

import org.osivia.services.calendar.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;

/**
 * Calendar data.
 * 
 * @author Cédric Krommenhoek
 */
public class CalendarData {

    /** Period type. */
    private PeriodTypes periodType;
    /** Generator. */
    private ICalendarGenerator generator;

    /** Selected date. */
    private Date selectedDate;
    /** Start date. */
    private Date startDate;
    /** End date. */
    private Date endDate;
    /** Display date. */
    private String displayDate;


    /**
     * Default constructor.
     */
    public CalendarData() {
        super();
    }


    /**
     * Getter for periodType.
     *
     * @return the periodType
     */
    public PeriodTypes getPeriodType() {
        return this.periodType;
    }

    /**
     * Setter for periodType.
     *
     * @param periodType the periodType to set
     */
    public void setPeriodType(PeriodTypes periodType) {
        this.periodType = periodType;
    }

    /**
     * Getter for generator.
     *
     * @return the generator
     */
    public ICalendarGenerator getGenerator() {
        return this.generator;
    }

    /**
     * Setter for generator.
     *
     * @param generator the generator to set
     */
    public void setGenerator(ICalendarGenerator generator) {
        this.generator = generator;
    }

    /**
     * Getter for selectedDate.
     *
     * @return the selectedDate
     */
    public Date getSelectedDate() {
        return this.selectedDate;
    }

    /**
     * Setter for selectedDate.
     *
     * @param selectedDate the selectedDate to set
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * Getter for startDate.
     *
     * @return the startDate
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Setter for startDate.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for endDate.
     *
     * @return the endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for endDate.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for displayDate.
     *
     * @return the displayDate
     */
    public String getDisplayDate() {
        return this.displayDate;
    }

    /**
     * Setter for displayDate.
     *
     * @param displayDate the displayDate to set
     */
    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

}
