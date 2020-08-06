package org.osivia.services.rss.common.validator;

import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.service.ContainerRssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Container form validator.
 * 
 * @author Frédéric Boudan
 * 
 * @see Validator
 */
@Component
public class ContainerFormValidator implements Validator {


    /** Portlet service. */
    @Autowired
    private ContainerRssService service;

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
			if(container.getMap().contains(container.getName())) {
				errors.rejectValue("name", "Duplicated");
			}
			
			if (!errors.hasFieldErrors()) {
				if(!container.getPath().startsWith("/")) {
					errors.rejectValue("path", "invalid");
				} else if(this.service.getPathForder(errors, container)) {
					errors.rejectValue("path", "invalid");
				}
			}
		}
		
    }

}
