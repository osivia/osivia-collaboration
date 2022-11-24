package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorTemporaryAttachedPicture;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Document edition portlet repository interface.
 *
 * @param <T> document edition form type
 * @author Cédric Krommenhoek
 * @see DocumentEditionCommonRepository
 */
public interface DocumentEditionRepository<T extends AbstractDocumentEditionForm> extends DocumentEditionCommonRepository<T> {

    /**
     * Get repository parameterized type.
     *
     * @return type
     */
    Class<T> getParameterizedType();


    /**
     * Check if current repository matches.
     *
     * @param documentType document type
     * @param creation     document creation indicator
     * @return true if current repository matches
     */
    boolean matches(String documentType, boolean creation);


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
     */
    String getViewPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Validate document edition form.
     *
     * @param form   document edition form
     * @param errors validation errors
     */
    void validate(AbstractDocumentEditionForm form, Errors errors);


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
     * Save document.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @param pictures                temporary attached pictures
     */
    void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, List<EditorTemporaryAttachedPicture> pictures) throws PortletException, IOException;


    /**
     * Customize document properties.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @param creation                document creation indicator
     * @param properties              document properties
     * @param binaries                document updated binaries
     */
    void customizeProperties(PortalControllerContext portalControllerContext, T form, boolean creation, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException;
}
