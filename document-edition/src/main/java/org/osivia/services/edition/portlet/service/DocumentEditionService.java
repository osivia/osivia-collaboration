package org.osivia.services.edition.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.DocumentEditionRepository;
import org.springframework.validation.BindingResult;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Document edition portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionService {

    /**
     * Base path window property.
     */
    String BASE_PATH_WINDOW_PROPERTY = "osivia.document.edition.base-path";
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
    /** Multiple files indicator window property. */
    String MULTIPLE_FILES_WINDOW_PROPERTY = "osivia.document.edition.multiple-files";
    /**
     * Modal indicator window property.
     */
    String MODAL_INDICATOR_WINDOW_PROPERTY = "osivia.document.edition.modal";


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
    DocumentEditionRepository<AbstractDocumentEditionForm> getRepository(String name);


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
     * Save document edition.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, BindingResult result) throws PortletException, IOException;


    /**
     * Cancel document edition.
     *
     * @param portalControllerContext portal controller context
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Serve editor.
     *
     * @param portalControllerContext portal controller context
     * @param editorId                editor identifier
     */
    void serveEditor(PortalControllerContext portalControllerContext, String editorId) throws PortletException, IOException;

}
