package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.AddToGroupForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Add members to group form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class AddToGroupFormValidator implements Validator {

    /**
     * Constructor.
     */
    public AddToGroupFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AddToGroupForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "localGroups", "NotEmpty");
    }

}
