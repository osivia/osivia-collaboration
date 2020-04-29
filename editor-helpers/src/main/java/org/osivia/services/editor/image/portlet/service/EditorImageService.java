package org.osivia.services.editor.image.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.services.editor.common.service.CommonService;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceDocumentForm;

import javax.portlet.PortletException;

/**
 * Editor image portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonService
 */
public interface EditorImageService extends CommonService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-editor-image-instance";

    /**
     * Source URL parameter.
     */
    String SRC_PARAMETER = "src";
    /**
     * Source URL window property.
     */
    String SRC_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + SRC_PARAMETER;
    /**
     * Alternate text parameter.
     */
    String ALT_PARAMETER = "alt";
    /**
     * Alternate text window property.
     */
    String ALT_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + ALT_PARAMETER;


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException;


    /**
     * Get attached image form.
     *
     * @param portalControllerContext portal controller context
     * @return attached image form
     */
    EditorImageSourceAttachedForm getAttachedForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Select attached image.
     *
     * @param portalControllerContext portal controller context
     * @param attachedForm            attached image form
     */
    void selectAttached(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm) throws PortletException;


    /**
     * Get image document form.
     *
     * @param portalControllerContext portal controller context
     * @return image document form
     */
    EditorImageSourceDocumentForm getDocumentForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Filter documents.
     *
     * @param portalControllerContext portal controller context
     * @param documentForm            image document form
     */
    void filterDocuments(PortalControllerContext portalControllerContext, EditorImageSourceDocumentForm documentForm) throws PortletException;


    /**
     * Select image document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     */
    void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
