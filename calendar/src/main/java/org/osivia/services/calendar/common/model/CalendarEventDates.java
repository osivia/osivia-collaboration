package org.osivia.services.calendar.common.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar event dates java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEventDates {

    /** Start date. */
    private String startDate;
    /** Start time. */
    private String startTime;
    /** End date. */
    private String endDate;
    /** End time. */
    private String endTime;
	

    /**
     * Constructor.
     */
	public CalendarEventDates() {
		super();
	}


    /**
     * Getter for startDate.
     * 
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Setter for startDate.
     * 
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for startTime.
     * 
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Setter for startTime.
     * 
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for endDate.
     * 
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter for endDate.
     * 
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for endTime.
     * 
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Setter for endTime.
     * 
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
