package org.osivia.services.calendar.edition.portlet.model.validation;

import javax.activation.MimeType;

import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.Picture;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Calendar form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class CalendarFormValidator implements Validator {

    /** Image mime primary type. */
    private static final String IMAGE_MIME_PRIMARY_TYPE = "image";


    /**
     * Constructor.
     */
    public CalendarFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return CalendarEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        // Calendar form
        CalendarEditionForm form = (CalendarEditionForm) target;

        // Title
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        // Vignette
        Picture vignette = form.getVignette();
        if (vignette.getTemporaryFile() != null) {
            // Mime type
            MimeType mimeType = vignette.getTemporaryMimeType();

            if ((mimeType == null) || !IMAGE_MIME_PRIMARY_TYPE.equals(mimeType.getPrimaryType())) {
                errors.rejectValue("vignette.upload", "InvalidType");
            }
        }
    }

}
