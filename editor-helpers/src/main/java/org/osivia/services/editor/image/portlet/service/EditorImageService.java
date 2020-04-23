package org.osivia.services.editor.image.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.ImageSourceType;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Editor image portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface EditorImageService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-editor-image-instance";

    /**
     * Source URL window property.
     */
    String SRC_WINDOW_PROPERTY = "osivia.editor.src";
    /**
     * Alternate text window property.
     */
    String ALT_WINDOW_PROPERTY = "osivia.editor.alt";


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get image source types.
     *
     * @param portalControllerContext portal controller context
     * @return image source types
     */
    List<ImageSourceType> getSourceTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException;

}
