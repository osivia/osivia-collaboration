package org.osivia.services.editor.link.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.services.editor.common.service.CommonService;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;

import javax.portlet.PortletException;

/**
 * Editor link portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonService
 */
public interface EditorLinkService extends CommonService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-editor-link-instance";

    /**
     * URL parameter.
     */
    String URL_PARAMETER = "url";
    /**
     * URL window property.
     */
    String URL_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + URL_PARAMETER;
    /**
     * Text parameter.
     */
    String TEXT_PARAMETER = "text";
    /**
     * Text window property.
     */
    String TEXT_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + TEXT_PARAMETER;
    /**
     * Title parameter.
     */
    String TITLE_PARAMETER = "title";
    /**
     * Title window property.
     */
    String TITLE_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + TITLE_PARAMETER;
    /**
     * Only text indicator parameter.
     */
    String ONLY_TEXT_PARAMETER = "onlyText";
    /**
     * Only text indicator window property.
     */
    String ONLY_TEXT_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + ONLY_TEXT_PARAMETER;


    /**
     * Save editor link form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    editor link form
     */
    void save(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException;


    /**
     * Unlink.
     *
     * @param portalControllerContext portal controller context
     * @param form                    editor link form
     */
    void unlink(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException;


    /**
     * Get editor link form.
     *
     * @param portalControllerContext portal controller context
     * @return editor link form
     */
    EditorLinkForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

}
