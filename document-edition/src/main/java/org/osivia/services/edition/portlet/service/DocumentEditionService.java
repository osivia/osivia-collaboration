package org.osivia.services.edition.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.DocumentEditionRepository;

import javax.portlet.PortletException;
import java.io.IOException;

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
     * Document type window property.
     */
    String EXTRACT_ARCHIVE_WINDOW_PROPERTY = "osivia.document.edition.extract-archive";

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
    DocumentEditionRepository getRepository(String name);


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
    void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;


    /**
     * Cancel document edition.
     *
     * @param portalControllerContext portal controller context
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;

}
