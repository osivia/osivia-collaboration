package org.osivia.services.rss.feedRss.portlet.validator;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
				new URL(form.getUrl()).toURI();
			} catch (URISyntaxException | MalformedURLException e) {
				errors.rejectValue("url", "Invalid");
			}
			
			if(form.getMap().containsValue(form.getUrl())) {
				errors.rejectValue("url", "Duplicated");
			}
		}
		
		// DisplayName feed 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");
		
		// Duplicated Name
		if(form.getMap().containsKey(form.getDisplayName())) {
			errors.rejectValue("displayName", "Duplicated");
		}
    }

    /* Returns true if url is valid */
    public static boolean isValid(String url) 
    { 
        /* Try creating a valid URL */
        try { 
            new URL(url).toURI(); 
            return true; 
        } 
          
        // If there was an Exception 
        // while creating URL object 
        catch (Exception e) { 
            return false; 
        } 
    } 
}
