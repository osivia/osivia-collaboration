package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.CreateTaskForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Create task form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class CreateTaskFormValidator implements Validator {

    /**
     * Constructor.
     */
    public CreateTaskFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CreateTaskForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "createdTitle", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "createdType", "NotEmpty");
    }

}
