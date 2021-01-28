package org.osivia.services.editor.image.portlet.model.validation;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

/**
 * Editor attached image source form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 * @see EditorImageSourceAttachedForm
 */
@Component
public class EditorImageSourceAttachedFormValidator implements Validator {

    /**
     * Constructor.
     */
    public EditorImageSourceAttachedFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return EditorImageSourceAttachedForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        EditorImageSourceAttachedForm attachedForm = (EditorImageSourceAttachedForm) target;

        // Upload
        MultipartFile upload = attachedForm.getUpload();
        if (upload != null) {
            // MIME type
            MimeType mimeType;
            try {
                mimeType = new MimeType(upload.getContentType());
            } catch (MimeTypeParseException e) {
                mimeType = null;
            }

            if ((mimeType == null) || !StringUtils.equals("image", mimeType.getPrimaryType())) {
                errors.rejectValue("upload", "invalid");
            }
        }
    }

}
