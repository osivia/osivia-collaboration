package org.osivia.services.edition.portlet.repository;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.NoteEditionForm;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;
import java.util.Map;

/**
 * Note edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionRepositoryImpl
 * @see NoteEditionForm
 */
@Repository
public class NoteEditionRepositoryImpl extends DocumentEditionRepositoryImpl<NoteEditionForm> {

    /**
     * Note content Nuxeo document property.
     */
    public static final String CONTENT_PROPERTY = "note:note";


    /**
     * Constructor.
     */
    public NoteEditionRepositoryImpl() {
        super();
    }


    @Override
    public Class<NoteEditionForm> getParameterizedType() {
        return NoteEditionForm.class;
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return "Note".equals(documentType);
    }


    @Override
    protected void customizeForm(PortalControllerContext portalControllerContext, Document document, NoteEditionForm form) {
        // Fullscreen indicator
        form.setFullscreen(true);

        if (document != null) {
            // Content
            String content = document.getString(CONTENT_PROPERTY);
            form.setContent(content);
        }
    }


    @Override
    protected void customizeValidation(NoteEditionForm form, Errors errors) {
        super.customizeValidation(form, errors);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "NotEmpty");
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "note";
    }


    @Override
    public void customizeProperties(PortalControllerContext portalControllerContext, NoteEditionForm form, boolean creation, PropertyMap properties, Map<String, List<Blob>> binaries) {
        // Content
        properties.set(CONTENT_PROPERTY, StringUtils.trimToNull(form.getContent()));
    }

}
