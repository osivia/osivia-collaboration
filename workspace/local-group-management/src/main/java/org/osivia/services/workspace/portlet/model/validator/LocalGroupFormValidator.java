package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Local group form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class LocalGroupFormValidator implements Validator {

    /**
     * Constructor.
     */
    public LocalGroupFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return LocalGroupForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");
    }

}
