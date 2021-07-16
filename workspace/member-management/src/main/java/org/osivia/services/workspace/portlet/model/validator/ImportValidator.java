package org.osivia.services.workspace.portlet.model.validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.ImportForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Import form validator.
 * 
 * @author Loïc Billon
 * @see Validator
 */
@Component
public class ImportValidator implements Validator {

    private static final int MAX_SIZE = 100;


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
    	
    	
    	if(form.getUpload().getSize() > 0) { // <= upload method
    		checkFile(errors, form); // Check file integrity
    	}
    	else if(form.getTemporaryFile() == null) { // <= save method
    	
	    	if(form.getUpload().getSize() <= 0) { // Check if a file is missing
	    		errors.rejectValue("upload", "INVALID_EMPTY_FILE", null);
	    	}

    	}
    }


	private void checkFile(Errors errors, ImportForm form) {
    
		
		String originalFilename = form.getUpload().getOriginalFilename();
		if(!StringUtils.endsWithIgnoreCase(originalFilename, ".csv")) {
			errors.rejectValue("upload", "INVALID_TYPE_FILE", null);

	    }
	    else {
	    	
			CSVParser parser;
			try {
				parser = CSVParser.parse(form.getUpload().getInputStream(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
				int size = parser.getRecords().size();
				if(size > MAX_SIZE) {
					errors.rejectValue("upload", "INVALID_FILE_MAX_SIZE", null);

				}
				
			} catch (IOException e) {
				errors.rejectValue("upload", "INVALID_FILE_MAX_SIZE", null);

			}
	    	
	    }

		

	}

}
