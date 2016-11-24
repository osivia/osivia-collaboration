package org.osivia.services.workspace.edition.portlet.model.validator;

import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WorkspaceEditionFormValidator implements Validator {

    /** Portlet service. */
    @Autowired
    private WorkspaceEditionService service;


    /**
     * Constructor.
     */
    public WorkspaceEditionFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return WorkspaceEditionForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        // Target form
        WorkspaceEditionForm form = (WorkspaceEditionForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        if (form.isRoot()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workspaceType", "NotEmpty");

            this.service.validate(errors, form);
        }
    }

}
