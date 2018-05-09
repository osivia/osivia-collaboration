package org.osivia.services.calendar.event.edition.portlet.model;

import org.osivia.services.calendar.common.model.AbstractCalendarEditionForm;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar edition form java-bean.
 *
 * @author Julien Barberet
 * @author Cédric Krommenhoek
 * @see AbstractCalendarEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEventEditionForm extends CalendarCommonEventForm {

    /** Calendar color. */
    private CalendarColor calendarColor;


    /**
     * Constructor.
     */
    public CalendarEventEditionForm() {
        super();
    }

    @Override
	public CalendarColor getColor() {
		return (CalendarColor) color;
	}

	public void setColor(CalendarColor color) {
		this.color = color;
	}

    /**
     * Getter for calendarColor.
     * 
     * @return the calendarColor
     */
    public CalendarColor getCalendarColor() {
        return calendarColor;
    }

    /**
     * Setter for calendarColor.
     * 
     * @param calendarColor the calendarColor to set
     */
    public void setCalendarColor(CalendarColor calendarColor) {
        this.calendarColor = calendarColor;
    }

}
