package org.osivia.services.rss.feedRss.portlet.validator;

import java.io.IOException;
import java.net.URL;

import org.osivia.services.rss.common.model.ContainerRssModel;
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
        return ContainerRssModel.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {

		// Form
		ContainerRssModel form = (ContainerRssModel) target;
		
		// URL feed 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "url", "NotEmpty");
		
		// validate url	
		if (!errors.hasFieldErrors()) {
			URL url = form.getUrl();
			try {
				url.openStream();
			} catch (IOException e) {
				errors.rejectValue("url", "Invalid");				
			}
		}		
	
		// PartId feed 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "partId", "NotEmpty");		
		
    }

}
