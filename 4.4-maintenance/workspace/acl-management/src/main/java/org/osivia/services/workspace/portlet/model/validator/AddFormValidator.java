package org.osivia.services.workspace.portlet.model.validator;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.services.workspace.portlet.model.AddForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Add form validator.
 *
 * @author Cédric Krommenhoek
 */
@Component
public class AddFormValidator implements Validator {

    /**
     * Constructor.
     */
    public AddFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AddForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        AddForm form = (AddForm) target;

        if (CollectionUtils.isEmpty(form.getIdentifiers())) {
            errors.rejectValue("identifiers", "NotEmpty");
        }
    }

}
