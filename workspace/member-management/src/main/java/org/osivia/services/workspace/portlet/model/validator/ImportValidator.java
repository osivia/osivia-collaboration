package org.osivia.services.workspace.portlet.model.validator;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.osivia.services.workspace.portlet.model.ImportForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Import form validator.
 * 
 * @author Loïc Billon
 * @see Validator
 */
@Component
public class ImportValidator implements Validator {

    /**
     * Constructor.
     */
    public ImportValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return ImportForm.class.isAssignableFrom(clazz);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
    	
    	ImportForm form = (ImportForm) target;
    	if(form.getUpload().getSize() <= 0) {
    		errors.rejectValue("upload", "INVALID_EMPTY_FILE", null);
    	}
    	
        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(form.getUpload().getContentType());
            
            if(!mimeType.getBaseType().equals("text/csv")) {
        		errors.rejectValue("upload", "INVALID_TYPE_FILE", null);

            }
            
        } catch (MimeTypeParseException e) {
    		errors.rejectValue("upload", "INVALID_TYPE_FILE", null);
        }
    	
    }

}
