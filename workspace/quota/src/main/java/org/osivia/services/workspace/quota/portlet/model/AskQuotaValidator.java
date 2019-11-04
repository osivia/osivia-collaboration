package org.osivia.services.workspace.quota.portlet.model;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AskQuotaValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
        return AskQuotaForm.class.isAssignableFrom(clazz);

	}

	@Override
	public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "requestMsg", "NotEmpty");

	}

}
