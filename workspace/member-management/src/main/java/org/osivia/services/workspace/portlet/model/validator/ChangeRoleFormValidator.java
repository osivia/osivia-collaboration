package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.ChangeRoleForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Change role form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class ChangeRoleFormValidator implements Validator {

    /**
     * Constructor.
     */
    public ChangeRoleFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return ChangeRoleForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "role", "NotEmpty");
    }

}
