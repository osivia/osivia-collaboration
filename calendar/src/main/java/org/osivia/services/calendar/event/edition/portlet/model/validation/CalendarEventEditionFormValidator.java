package org.osivia.services.calendar.event.edition.portlet.model.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.osivia.services.calendar.event.edition.portlet.service.CalendarEventEditionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Calendar event edition form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class CalendarEventEditionFormValidator implements Validator {

    /** Date format. */
    private final DateFormat dateFormat;
    /** Time format. */
    private final DateFormat timeFormat;


    /**
     * Constructor.
     */
    public CalendarEventEditionFormValidator() {
        super();

        // Date format
        this.dateFormat = new SimpleDateFormat(CalendarEventEditionService.DATE_FORMAT_PATTERN);
        // Time format
        this.timeFormat = new SimpleDateFormat(CalendarEventEditionService.TIME_FORMAT_PATTERN);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CalendarCommonEventForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        // Form
        CalendarCommonEventForm form = (CalendarCommonEventForm) target;

        // Title
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        // Start date
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dates.startDate", "NotEmpty");
        if (StringUtils.isNotBlank(form.getDates().getStartDate())) {
            try {
                this.dateFormat.parse(form.getDates().getStartDate());
            } catch (ParseException e) {
                errors.rejectValue("dates.startDate", "Invalid");
            }
        }

        // End date
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dates.endDate", "NotEmpty");
        if (StringUtils.isNotBlank(form.getDates().getEndDate())) {
            try {
                this.dateFormat.parse(form.getDates().getEndDate());
            } catch (ParseException e) {
                errors.rejectValue("dates.endDate", "Invalid");
            }
        }

        if (BooleanUtils.isNotTrue(form.isAllDay())) {
            // Start time
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dates.startTime", "NotEmpty");
            if (StringUtils.isNotBlank(form.getDates().getEndDate())) {
                try {
                    this.timeFormat.parse(form.getDates().getStartTime());
                } catch (ParseException e) {
                    errors.rejectValue("dates.startTime", "Invalid");
                }
            }

            // End time
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dates.endTime", "NotEmpty");
            if (StringUtils.isNotBlank(form.getDates().getEndTime())) {
                try {
                    this.timeFormat.parse(form.getDates().getEndTime());
                } catch (ParseException e) {
                    errors.rejectValue("dates.endTime", "Invalid");
                }
            }
        }
    }

}
