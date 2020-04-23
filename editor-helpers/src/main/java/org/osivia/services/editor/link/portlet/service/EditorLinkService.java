package org.osivia.services.editor.link.portlet.service;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.FilterType;
import org.osivia.services.editor.link.portlet.model.UrlType;

import net.sf.json.JSONObject;

/**
 * Editor link portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface EditorLinkService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-editor-link-instance";

    /** URL window property. */
    String URL_PROPERTY = "osivia.editor.url";
    /** Text window property. */
    String TEXT_PROPERTY = "osivia.editor.text";
    /** Title window property. */
    String TITLE_PROPERTY = "osivia.editor.title";
    /** Only text indicator window property. */
    String ONLY_TEXT_PROPERTY = "osivia.editor.onlyText";
    /** Base path window property. */
    String BASE_PATH_PROPERTY = "osivia.editor.basePath";


    /**
     * Save editor link form.
     *
     * @param portalControllerContext portal controller context
     * @param form editor link form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException;


    /**
     * Unlink.
     *
     * @param portalControllerContext portal controller context
     * @param form editor link form
     * @throws PortletException
     */
    void unlink(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException;


    /**
     * Search documents.
     *
     * @param portalControllerContext portal controller context
     * @param filter search filter
     * @param page search pagination page number
     * @return search result JSON object
     * @throws PortletException
     */
    JSONObject searchDocuments(PortalControllerContext portalControllerContext, String filter, int page) throws PortletException;


    /**
     * Get editor link form.
     *
     * @param portalControllerContext portal controller context
     * @return editor link form
     * @throws PortletException
     */
    EditorLinkForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get URL types.
     *
     * @param portalControllerContext portal controller context
     * @return URL types
     * @throws PortletException
     */
    List<UrlType> getUrlTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get filter types.
     *
     * @param portalControllerContext portal controller context
     * @return filter types
     * @throws PortletException
     * @throws IOException
     */
    List<FilterType> getFilterTypes(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Resolve view path.
     *
     * @param portalControllerContext portal controller context
     * @param name view name
     * @return path
     * @throws PortletException
     */
    String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException;

}
