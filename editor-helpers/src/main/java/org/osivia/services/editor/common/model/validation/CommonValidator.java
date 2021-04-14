package org.osivia.services.editor.common.model.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Validator abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
public abstract class CommonValidator implements Validator {

    /**
     * Validate URL.
     *
     * @param errors errors
     * @param field  URL field
     * @param value  URL value
     */
    protected void validateUrl(Errors errors, String field, String value) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, "empty");

        if (StringUtils.isNotBlank(value)) {
            try {
                if (StringUtils.startsWith(value, "/")) {
                    // Relative
                    URL baseUrl = new URL("http://www.example.com");
                    new URL(baseUrl, value);
                } else {
                    // Absolute
                    new URL(value);
                }
            } catch (MalformedURLException e) {
                errors.rejectValue(field, "malformed");
            }
        }
    }

}
