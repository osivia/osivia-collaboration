package org.osivia.services.editor.link.portlet.model.validator;

import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.UrlType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Editor link form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class EditorLinkFormValidator implements Validator {

    /**
     * Constructor.
     */
    public EditorLinkFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return EditorLinkForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        EditorLinkForm form = (EditorLinkForm) target;
        if (UrlType.MANUAL.equals(form.getUrlType())) {
            ValidationUtils.rejectIfEmpty(errors, "manualUrl", "NotEmpty");
        } else if (UrlType.DOCUMENT.equals(form.getUrlType())) {
            ValidationUtils.rejectIfEmpty(errors, "documentWebId", "NotEmpty");
        }
    }
}
