package org.osivia.services.edition.portlet.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.DocumentEditionRepository;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Document edition portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionService {

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.document.edition.path";
    /**
     * Parent document path window property.
     */
    String PARENT_DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.document.edition.parent-path";
    /**
     * Document type window property.
     */
    String DOCUMENT_TYPE_WINDOW_PROPERTY = "osivia.document.edition.document-type";
    /**
     * Required primary MIME-type window property.
     */
    String REQUIRED_PRIMARY_MIME_TYPE_WINDOW_PROPERTY = "osivia.document.edition.required-primary-mime-type";
    /**
     * Extract archive indicator window property.
     */
    String EXTRACT_ARCHIVE_WINDOW_PROPERTY = "osivia.document.edition.extract-archive";
    /**
     * Fullscreen indicator window property.
     */
    String FULLSCREEN_WINDOW_PROPERTY = "osivia.document.edition.fullscreen";


    /**
     * Max upload size.
     */
    long MAX_UPLOAD_SIZE = NumberUtils.toLong(System.getProperty("osivia.filebrowser.max.upload.size"), 500L) * FileUtils.ONE_MB;


    /**
     * Get window properties.
     *
     * @param portalControllerContext portal controller context
     * @return window properties
     */
    DocumentEditionWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get document edition form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    AbstractDocumentEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Get portlet repository.
     *
     * @param name repository name
     * @return repository
     */
    DocumentEditionRepository<?> getRepository(String name);


    /**
     * Get view path.
     *
     * @param portalControllerContext portal controller context
     * @return view path
     */
    String getViewPath(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Upload document file.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void upload(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Restore document file.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void restore(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Upload document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void uploadVignette(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Delete document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void deleteVignette(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Restore document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void restoreVignette(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Validate document edition form.
     *
     * @param form   document edition form
     * @param errors errors
     */
    void validate(AbstractDocumentEditionForm form, Errors errors);


    /**
     * Save document edition.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Cancel document edition.
     *
     * @param portalControllerContext portal controller context
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Vignette preview.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void vignettePreview(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Serve editor.
     *
     * @param portalControllerContext portal controller context
     * @param editorId                editor identifier
     */
    void serveEditor(PortalControllerContext portalControllerContext, String editorId) throws PortletException, IOException;

}
