package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * File edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionRepositoryImpl
 * @see FileEditionForm
 */
@Repository("File")
public class FileEditionRepositoryImpl extends AbstractDocumentEditionRepositoryImpl<FileEditionForm> {

    /**
     * Constructor.
     */
    public FileEditionRepositoryImpl() {
        super();
    }


    @Override
    public FileEditionForm getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException {
        return super.getForm(portalControllerContext, windowProperties, FileEditionForm.class);
    }


    @Override
    void customizeForm(PortalControllerContext portalControllerContext, Document document, FileEditionForm form) {
        // TODO
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) {
        return "file";
    }


    @Override
    void customizeProperties(PortalControllerContext portalControllerContext, FileEditionForm form, PropertyMap properties) {
        // TODO
    }

}
