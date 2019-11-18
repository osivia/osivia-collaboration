package org.osivia.services.rss.feedRss.portlet.validator;

import java.io.IOException;
import java.net.URL;

import org.osivia.services.rss.common.model.FeedRssModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Feed form validator.
 * 
 * @author Frédéric Boudan
 * 
 * @see Validator
 */
@Component
public class FeedFormValidator implements Validator {

    /**
     * Constructor.
     */
    public FeedFormValidator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FeedRssModel.class.isAssignableFrom(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {

		// Form
		FeedRssModel form = (FeedRssModel) target;
		
		// URL feed 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "NotEmpty");
		
		// validate url	
		if (!errors.hasFieldErrors()) {
			try {
				URL url = new URL(form.getUrl()); 
				url.openStream();
			} catch (IOException e) {
				errors.rejectValue("url", "Invalid");
			}
			
			if(form.getMap().contains(form.getUrl())) {
				errors.rejectValue("url", "Duplicated");
			}
		}
		
		
    }

}
