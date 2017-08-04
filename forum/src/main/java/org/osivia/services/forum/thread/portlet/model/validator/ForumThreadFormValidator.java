package org.osivia.services.forum.thread.portlet.model.validator;

import org.osivia.services.forum.thread.portlet.model.ForumThreadForm;
import org.osivia.services.forum.thread.portlet.model.ForumThreadObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Forum thread form validator.
 *
 * @author Cédric Krommenhoek
 * @see Validator
 */
@Component
public class ForumThreadFormValidator implements Validator {

    /**
     * Constructor.
     */
    public ForumThreadFormValidator() {
        super();
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return ForumThreadForm.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        // Forum thread form
        ForumThreadForm form = (ForumThreadForm) target;

        // Edited forum thread post
        ForumThreadObject post = form.getPosts().getEditedPost();

        if (post == null) {
            // Reply

            // Message
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reply.message", "NotEmpty");
        } else {
            // Post edition

            // Message
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "posts.editedPost.message", "NotEmpty");
        }
    }

}
