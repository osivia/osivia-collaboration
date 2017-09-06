package org.osivia.services.forum.edition.portlet.model.validator;

import javax.activation.MimeType;

import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.Vignette;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Forum edition form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class ForumEditionFormValidator implements Validator {

    /** Image mime primary type. */
    private static final String IMAGE_MIME_PRIMARY_TYPE = "image";


    /**
     * Constructor.
     */
    public ForumEditionFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return ForumEditionForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        // Forum edition form
        ForumEditionForm form = (ForumEditionForm) target;

        // Title
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        // Vignette
        Vignette vignette = form.getVignette();
        if (vignette.getTemporaryFile() != null) {
            // Mime type
            MimeType mimeType = vignette.getMimeType();

            if ((mimeType == null) || !IMAGE_MIME_PRIMARY_TYPE.equals(mimeType.getPrimaryType())) {
                errors.rejectValue("vignette.upload", "InvalidType");
            }
        }

        if (ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(form.getDocumentType())) {
            // Message
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "NotEmpty");
        }
    }

}
