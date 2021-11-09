package org.osivia.services.edition.portlet.model.validator;

import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.repository.DocumentEditionRepository;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;


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
        // Form
        AbstractDocumentEditionForm form = (AbstractDocumentEditionForm) target;
        
        // Repository
        String name = form.getName();
        if(form.getExtractArchive()) {
        	name = "Zip";
        }
        
        DocumentEditionRepository repository = this.service.getRepository(name);

        repository.validate(form, errors);
    }

}
