package org.osivia.services.edition.portlet.model.validator;

import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Document edition form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class DocumentEditionFormValidator implements Validator {

    /**
     * Constructor.
     */
    public DocumentEditionFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return AbstractDocumentEditionForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        // TODO
    }

}
