package org.osivia.services.workspace.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Invitations creation form validator.
 * 
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class InvitationsCreationFormValidator implements Validator {

    /**
     * Constructor.
     */
    public InvitationsCreationFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(InvitationsCreationForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "identifiers", "NotEmpty");
    }

}
