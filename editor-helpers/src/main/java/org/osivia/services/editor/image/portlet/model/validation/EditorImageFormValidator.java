package org.osivia.services.editor.image.portlet.model.validation;

import org.osivia.services.editor.common.model.validation.CommonValidator;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Editor image form validator.
 *
 * @author Cédric Krommenhoek
 * @see CommonValidator
 */
@Component
public class EditorImageFormValidator extends CommonValidator {

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
        EditorImageForm form = (EditorImageForm) target;

        this.validateUrl(errors, "url", form.getUrl());
    }

}
