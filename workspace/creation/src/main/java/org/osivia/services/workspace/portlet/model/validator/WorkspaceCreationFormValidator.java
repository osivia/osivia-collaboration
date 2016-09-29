package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Workspace creation form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class WorkspaceCreationFormValidator implements Validator {

    /**
     * Constructor.
     */
    public WorkspaceCreationFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(WorkspaceCreationForm.class);
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
