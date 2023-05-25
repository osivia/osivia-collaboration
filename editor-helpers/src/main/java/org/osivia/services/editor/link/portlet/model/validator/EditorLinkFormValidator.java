package org.osivia.services.editor.link.portlet.model.validator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.common.model.validation.CommonValidator;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Editor link form validator.
 *
 * @author Cédric Krommenhoek
 * @see CommonValidator
 */
@Component
public class EditorLinkFormValidator extends CommonValidator {

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

        this.validateUrl(errors, "url", form.getUrl());
    }


    @Override
    protected void validateUrl(Errors errors, String field, String value) {
        if (StringUtils.startsWith(value, "#")) {
            // Anchor
            try {
                URL baseUrl = new URL("http://www.example.com");
                new URL(baseUrl, value);
            } catch (MalformedURLException e) {
                errors.rejectValue(field, "malformed");
            }
        } else {
            super.validateUrl(errors, field, value);
        }
    }

}
