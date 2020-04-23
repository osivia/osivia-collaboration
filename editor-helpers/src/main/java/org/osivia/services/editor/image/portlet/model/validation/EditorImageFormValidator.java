package org.osivia.services.editor.image.portlet.model.validation;

import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Editor image form validator.
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class EditorImageFormValidator implements Validator {

    /**
     * Constructor.
     */
    public EditorImageFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return EditorImageForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "empty");
    }

}
