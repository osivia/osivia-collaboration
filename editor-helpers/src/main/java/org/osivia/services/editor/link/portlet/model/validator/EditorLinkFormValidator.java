package org.osivia.services.editor.link.portlet.model.validator;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
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


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return EditorLinkForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        EditorLinkForm form = (EditorLinkForm) target;
        if (UrlType.MANUAL.equals(form.getUrlType())) {
        	ValidationUtils.rejectIfEmpty(errors, "manualUrl", "empty");
        	if (StringUtils.isNotBlank(form.getManualUrl())) {
        		try {
        			if (StringUtils.startsWith(form.getManualUrl(), "/")) {
        				// Relative
        				URL baseUrl = new URL("http://www.example.com");
						new URL(baseUrl, form.getManualUrl());
        			} else {
        				// Absolute
        				new URL(form.getManualUrl());
        			}
    			} catch (MalformedURLException e) {
    				errors.rejectValue("manualUrl", "malformed");
    			}
        	}
        } else if (UrlType.DOCUMENT.equals(form.getUrlType())) {
            ValidationUtils.rejectIfEmpty(errors, "documentWebId", "empty");
        }
    }
}
