package org.osivia.services.calendar.synchronization.edition.portlet.model.validation;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.calendar.synchronization.edition.portlet.model.CalendarSynchronizationEditionForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Synchronization source edition form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class CalendarSynchronizationEditionFormValidator implements Validator {

    /**
     * Constructor.
     */
    public CalendarSynchronizationEditionFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CalendarSynchronizationEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        CalendarSynchronizationEditionForm form = (CalendarSynchronizationEditionForm) target;

        // URL
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "NotEmpty");
        if (StringUtils.isNotBlank(form.getUrl())) {
            try {
                new URL(form.getUrl());
            } catch (MalformedURLException e) {
                errors.rejectValue("url", "Invalid");
            }
        }

        // Display name
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");
    }

}
