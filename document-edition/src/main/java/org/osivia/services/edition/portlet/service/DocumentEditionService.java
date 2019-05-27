package org.osivia.services.edition.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;

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
     * Get view path.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @return view path
     */
    String getViewPath(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException;


    /**
     * Save document.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException;

}
