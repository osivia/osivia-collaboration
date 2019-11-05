package org.osivia.services.workspace.filebrowser.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.*;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * File browser portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface FileBrowserService {

    /**
     * Document base path window property.
     */
    String BASE_PATH_WINDOW_PROPERTY = "osivia.file-browser.base-path";
    /**
     * Document path window property.
     */
    String PATH_WINDOW_PROPERTY = Constants.WINDOW_PROP_URI;
    /**
     * NXQL request window property.
     */
    String NXQL_WINDOW_PROPERTY = "osivia.file-browser.nxql";
    /**
     * BeanShell indicator window property.
     */
    String BEANSHELL_WINDOW_PROPERTY = "osivia.file-browser.beanshell";
    /**
     * List mode indicator window property.
     */
    String LIST_MODE_WINDOW_PROPERTY = "osivia.file-browser.list-mode";
    /**
     * Default sort field window property.
     */
    String DEFAULT_SORT_FIELD_WINDOW_PROPERTY = "osivia.file-browser.default-sort-field";


    /**
     * Get window properties.
     *
     * @param portalControllerContext portal controller context
     * @return window properties
     */
    FileBrowserWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set window properties.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     */
    void setWindowProperties(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) throws PortletException;


    /**
     * Get view.
     *
     * @param portalControllerContext portal controller context
     * @param viewId                  provided view identifier, may be null
     * @return view
     */
    FileBrowserView getView(PortalControllerContext portalControllerContext, String viewId) throws PortletException;


    /**
     * Save view.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param view                    view
     */
    void saveView(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserView view) throws PortletException;


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort file browser items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param field                   sort field
     * @param alt                     alternative sort indicator
     */
    void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSortField field, boolean alt) throws PortletException;


    /**
     * Get file browser sort fields.
     *
     * @param portalControllerContext portal controller context
     * @return sort fields
     */
    List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get file browser sort fields.
     *
     * @param portalControllerContext portal controller context
     * @param listMode                list mode indicator
     * @return sort fields
     */
    List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext, boolean listMode) throws PortletException;


    /**
     * Get file browser sort field.
     *
     * @param portalControllerContext portal controller context
     * @param fieldId                 sort field identifier
     * @return sort field
     */
    FileBrowserSortField getSortField(PortalControllerContext portalControllerContext, String fieldId) throws PortletException;


    /**
     * Get file browser toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param indexes                 selected items indexes
     * @param viewId                  provided view identifier, may be null
     * @return DOM element
     */
    Element getToolbar(PortalControllerContext portalControllerContext, FileBrowserForm form, List<String> indexes, String viewId) throws PortletException;


    /**
     * Duplicate document.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param path                    document path
     */
    void duplicate(PortalControllerContext portalControllerContext, FileBrowserForm form, String path) throws PortletException;


    /**
     * Get bulk download content.
     *
     * @param portalControllerContext portal controller context
     * @param paths                   document paths
     * @return content
     */
    FileBrowserBulkDownloadContent getBulkDownload(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Drop.
     *
     * @param portalControllerContext portal controller context
     * @param sourceIdentifiers       source identifiers
     * @param targetIdentifier        target identifier
     */
    void drop(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException;


    /**
     * Upload.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void upload(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException, IOException;


    /**
     * Update menubar.
     *
     * @param portalControllerContext portal controller context
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * End upload.
     *
     * @param portalControllerContext portal controller context
     */
    void endUpload(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get location breadcrumb DOM element
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return DOM element
     */
    Element getLocationBreadcrumb(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
