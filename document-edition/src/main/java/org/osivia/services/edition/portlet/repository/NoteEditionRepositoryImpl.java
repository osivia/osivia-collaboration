package org.osivia.services.edition.portlet.repository;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.NoteEditionForm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Note edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see NoteEditionForm
 */
@Repository
public class NoteEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<NoteEditionForm> {

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
        // Content
        String content = document.getString("note:note");
        form.setContent(content);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "note";
    }


    @Override
    protected void customizeProperties(PortalControllerContext portalControllerContext, NoteEditionForm form, PropertyMap properties, Map<String, List<Blob>> binaries) {
        // Content
        properties.set("note:note", StringUtils.trimToNull(form.getContent()));
    }

}
