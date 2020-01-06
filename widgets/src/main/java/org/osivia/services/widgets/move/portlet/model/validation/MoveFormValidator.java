package org.osivia.services.widgets.move.portlet.model.validation;

import org.osivia.services.widgets.move.portlet.model.MoveForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Move portlet form validator.
 * @see Validator
 */
@Component
public class MoveFormValidator implements Validator {

    /**
     * Constructor.
     */
    public MoveFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return MoveForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "targetPath", "NotEmpty");
    }

}
