package org.osivia.services.widgets.rename.portlet.model.validation;

import org.osivia.services.widgets.rename.portlet.model.RenameForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Rename form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class RenameFormValidator implements Validator {

    /**
     * Constructor.
     */
    public RenameFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return RenameForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");
    }

}
