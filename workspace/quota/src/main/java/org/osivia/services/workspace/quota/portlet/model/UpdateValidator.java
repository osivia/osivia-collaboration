package org.osivia.services.workspace.quota.portlet.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
        return UpdateForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		UpdateForm form = (UpdateForm) target;
		
		try {
			
			if(StringUtils.isNotBlank(form.getSize())) {
		        Long l = Long.parseLong(form.getSize());

		        if(l < 1) {
			        errors.rejectValue("size", "QUOTA_NOT_A_NUMBER");
		        }
			}
			
	    } catch (NumberFormatException | NullPointerException nfe) {
	        
	        errors.rejectValue("size", "QUOTA_NOT_A_NUMBER");

	    }
		

		
	}

}
