package org.osivia.services.workspace.common.portlet.model.validator;

import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Task creation form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class TaskCreationFormValidator implements Validator {

    /**
     * Constructor.
     */
    public TaskCreationFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return TaskCreationForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "NotEmpty");
    }

}
