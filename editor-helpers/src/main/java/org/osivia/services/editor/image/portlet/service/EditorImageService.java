package org.osivia.services.editor.image.portlet.service;

import org.apache.commons.io.FileUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.services.editor.common.service.CommonService;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm;

import javax.portlet.PortletException;
import java.io.IOException;

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
     * Creation indicator parameter.
     */
    String CREATION_PARAMETER = "creation";
    /**
     * Creation indicator window property.
     */
    String CREATION_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + CREATION_PARAMETER;
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
     * Height parameter.
     */
    String HEIGHT_PARAMETER = "height";
    /**
     * Height window property.
     */
    String HEIGHT_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + HEIGHT_PARAMETER;
    /**
     * Width parameter.
     */
    String WIDTH_PARAMETER = "width";
    /**
     * Width window property.
     */
    String WIDTH_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + WIDTH_PARAMETER;

    /**
     * Max upload size.
     */
    long MAX_UPLOAD_SIZE = 100L * FileUtils.ONE_MB;


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
    void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException, IOException;


    /**
     * Get attached image form.
     *
     * @param portalControllerContext portal controller context
     * @return attached image form
     */
    EditorImageSourceAttachedForm getAttachedForm(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Add attached image.
     *
     * @param portalControllerContext portal controller context
     * @param attachedForm            attached image form
     */
    void addAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm) throws PortletException, IOException;


    /**
     * Select attached image.
     *
     * @param portalControllerContext portal controller context
     * @param attachedForm            attached image form
     * @param index                   attached image index
     */
    void selectAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm, int index) throws PortletException, IOException;


    /**
     * Delete attached image.
     *
     * @param portalControllerContext portal controller context
     * @param attachedForm            attached image form
     * @param index                   attached image index
     */
    void deleteAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm, int index) throws PortletException;


    /**
     * Serve image preview.
     *
     * @param portalControllerContext portal controller context
     */
    void serveImagePreview(PortalControllerContext portalControllerContext) throws PortletException, IOException;

}
