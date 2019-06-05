package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Document edition portlet repository interface.
 *
 * @param <T> document edition form type
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionRepository<T extends AbstractDocumentEditionForm> {

    /**
     * Get document context.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document context
     */
    NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get document edition form.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return form
     */
    T getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException;


    /**
     * Get view path.
     *
     * @param portalControllerContext portal controller context
     * @return view path
     * @throws PortletException
     */
    String getViewPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Validate document edition form.
     *
     * @param form   document edition form
     * @param errors errors
     */
    void validate(T form, Errors errors);


    /**
     * Upload document file.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void upload(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException;


    /**
     * Restore document file.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void restore(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException;


    /**
     * Save document.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    void save(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException;

}
