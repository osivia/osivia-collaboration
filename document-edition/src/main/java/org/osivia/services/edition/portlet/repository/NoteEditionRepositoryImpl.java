package org.osivia.services.edition.portlet.repository;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.NoteEditionForm;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Note edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see NoteEditionForm
 */
@Repository("Note")
public class NoteEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<NoteEditionForm> {

    /**
     * Constructor.
     */
    public NoteEditionRepositoryImpl() {
        super();
    }


    @Override
    public NoteEditionForm getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException {
        return super.getForm(portalControllerContext, windowProperties, NoteEditionForm.class);
    }


    @Override
    void customizeForm(PortalControllerContext portalControllerContext, Document document, NoteEditionForm form) {
        // Content
        String content = document.getString("note:note");
        form.setContent(content);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "note";
    }


    @Override
    void customizeProperties(PortalControllerContext portalControllerContext, NoteEditionForm form, PropertyMap properties) {
        // Content
        properties.set("note:note", StringUtils.trimToNull(form.getContent()));
    }

}
