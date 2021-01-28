package org.osivia.services.editor.common.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;

import javax.portlet.PortletException;

/**
 * Portlet common service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface CommonService {

    /**
     * Base path window property.
     */
    String BASE_PATH_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + "basePath";
    /**
     * Current document path window property.
     */
    String PATH_WINDOW_PROPERTY = EditorService.WINDOW_PROPERTY_PREFIX + "path";



    /**
     * Resolve view path.
     *
     * @param portalControllerContext portal controller context
     * @param name                    view name
     * @return path
     * @throws PortletException
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;

}
