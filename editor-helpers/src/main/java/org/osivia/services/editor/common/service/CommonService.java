package org.osivia.services.editor.common.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.services.editor.common.model.SourceDocumentForm;

import javax.portlet.PortletException;
import java.io.IOException;

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
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;


    /**
     * Get source document form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    SourceDocumentForm getSourceDocumentForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Serve search results.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  search filter
     * @param scope                   search scope
     */
    void serveSearchResults(PortalControllerContext portalControllerContext, String filter, String scope) throws PortletException, IOException;


    /**
     * Select document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     */
    void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException, IOException;

}
