package org.osivia.services.rss.container.portlet.validator;

import java.util.List;

import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.model.FeedRssModel;
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
public class ContainerFormValidator implements Validator {

    /**
     * Constructor.
     */
    public ContainerFormValidator() {
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
    	ContainerRssModel container =  (ContainerRssModel) target;
    	
		// Container Name 
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
		
		// Container Path
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "path", "NotEmpty");
		
		// Duplicated Container
		if (!errors.hasFieldErrors()) {
			if(container.getMap().containsValue(container.getName())) {
				errors.rejectValue("name", "Duplicated");
			}
		}
    }

}
