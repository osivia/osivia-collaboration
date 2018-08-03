package org.osivia.services.calendar.view.portlet.model.calendar;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.services.calendar.view.portlet.service.CalendarViewService;
import org.osivia.services.calendar.view.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar data.
 * 
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
    /** Scroll position of the day or week view */
    private String scrollViewDayWeek;
    /** Scroll position of the month view */
    private String scrollViewMonth;
    /** Agenda background color*/
    private String agendaBackgroundColor;
    /** Read only indicator. */
    private boolean readOnly;


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
     * Getter for scroll position of the day and week view
     * @return scroll position of the day and week view
     */
	public String getScrollViewDayWeek() {
		return scrollViewDayWeek;
	}


	/**
	 * Setter for scroll position of the day and week view
	 * @param scroll position of the day and week view
	 */
	public void setScrollViewDayWeek(String scrollViewDayWeek) {
		this.scrollViewDayWeek = scrollViewDayWeek;
	}

	/**
     * Getter for scroll position of the month view
     * @return scroll position of the month view
     */
	public String getScrollViewMonth() {
		return scrollViewMonth;
	}

	/**
	 * Setter for scroll position of the month view
	 * @param scroll position of the month view
	 */
	public void setScrollViewMonth(String scrollViewMonth) {
		this.scrollViewMonth = scrollViewMonth;
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
     * Getter for the start date in yyyy,MM,dd format
     */
    public String getStartDateToString()
    {
    	if (this.startDate != null)
    	{
    		return CalendarViewService.SELECTED_DATE_FORMAT.format(this.startDate);
    	}
    	return null;
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
     * End date calculation
     *
     * @return the endDate
     */
    public Date getEndDate() {
    	if (endDate == null)
    	{
	    	if (PeriodTypes.DAY == periodType)
	    	{
	    		endDate= DateUtils.addDays(startDate, 1);
	    	} 
	    	else if (PeriodTypes.WEEK == periodType)
	    	{
	    		endDate= DateUtils.addDays(startDate, 7);
	    	} 
	    	else if (PeriodTypes.MONTH == periodType)
	    	{
	    		endDate= DateUtils.addMonths(startDate, 1);
	    	}
    	}
    	
        return endDate;
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
	 * @return the agendaBackgroundColor
	 */
	public String getAgendaBackgroundColor() {
		return agendaBackgroundColor;
	}

	/**
	 * @param agendaBackgroundColor the agendaBackgroundColor to set
	 */
	public void setAgendaBackgroundColor(String agendaBackgroundColor) {
		this.agendaBackgroundColor = agendaBackgroundColor;
	}

    /**
     * Getter for readOnly.
     * 
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Setter for readOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
