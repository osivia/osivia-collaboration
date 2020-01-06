package org.osivia.services.rss.feedRss.portlet.validator;

import java.io.IOException;
import java.net.URL;

import org.osivia.services.rss.common.model.FeedRssModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Modif form validator.
 * 
 * @author Frédéric Boudan
 * 
 * @see Validator
 */
@Component
public class ModifFormValidator implements Validator {

    /**
     * Constructor.
     */
    public ModifFormValidator() {
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
		
		// DisplayName feed 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "displayName", "NotEmpty");
		
		// Duplicated Name
		if(form.getMap().containsKey(form.getDisplayName())) {
			errors.rejectValue("displayName", "Duplicated");
		}
    }

}
