package org.osivia.services.widgets.issued.portlet.model.validation;

import org.osivia.services.widgets.issued.portlet.model.IssuedForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Issued date form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class IssuedFormValidator implements Validator {

    /**
     * Constructor.
     */
    public IssuedFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return IssuedForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "date", "NotEmpty");
    }

}
