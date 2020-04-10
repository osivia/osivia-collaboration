package org.osivia.services.contact.portlet.model.validation;

import org.apache.commons.codec.binary.StringUtils;
import org.osivia.services.contact.portlet.model.Form;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Contact form validator.
 * 
 * @author Frédéric Boudan
 * 
 * @see Validator
 */
@Component
public class ContactFormValidator implements Validator {



    /**
     * Constructor.
     */
    public ContactFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Form.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
		// Form
		Form form = (Form) target;

		// from
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "from", "NotEmpty");

		if (!errors.hasFieldErrors()) {
			String mail = form.getFrom();
			boolean b1 = mail.contains("@") && mail.contains(".");

			if (!b1) {
				errors.rejectValue("from", "Invalid");
			}
		}

		// nom
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nom", "NotEmpty");

		// objet
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "object", "NotEmpty");

		// body
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "body", "NotEmpty");   
		
		// captcha
		if(!form.isCaptchaValidate()) {
			if(StringUtils.equals(form.getToken(), form.getCaptcha())) {
				form.setCaptchaValidate(true);
				
			}else {
				errors.rejectValue("captcha", "Invalid");
			}
		}
		
    }

}
