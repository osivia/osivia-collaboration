package org.osivia.services.workspace.filebrowser.portlet.service;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserBulkDownloadContent;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSort;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserView;

/**
 * File browser portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserService {

    /**
     * Get view.
     * 
     * @param portalControllerContext portal controller context
     * @param viewId provided view identifier, may be null
     * @return view
     * @throws PortletException
     */
    FileBrowserView getView(PortalControllerContext portalControllerContext, String viewId) throws PortletException;


    /**
     * Save view.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param view view
     * @throws PortletException
     */
    void saveView(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserView view) throws PortletException;


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort file browser items.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param sort sort
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSort sort, boolean alt) throws PortletException;


    /**
     * Get file browser toolbar DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param indexes selected items indexes
     * @param viewId provided view identifier, may be null
     * @return DOM element
     * @throws PortletException
     */
    Element getToolbar(PortalControllerContext portalControllerContext, FileBrowserForm form, List<String> indexes, String viewId) throws PortletException;


    /**
     * Duplicate document.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param paths documents path
     * @throws PortletException
     */
    void duplicate(PortalControllerContext portalControllerContext, FileBrowserForm form, String paths) throws PortletException;


    /**
     * Delete documents.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers document identifiers
     * @throws PortletException
     * @throws IOException
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException, IOException;


    /**
     * Get bulk download content.
     * 
     * @param portalControllerContext portal controller context
     * @param paths document paths
     * @return content
     * @throws PortletException
     * @throws IOException
     */
    FileBrowserBulkDownloadContent getBulkDownload(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Drop.
     * 
     * @param portalControllerContext portal controller context
     * @param sourceIdentifiers source identifiers
     * @param targetIdentifier target identifier
     * @throws PortletException
     */
    void drop(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException;


    /**
     * Upload.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    void upload(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException, IOException;


    /**
     * Update menubar.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;

    
    /**
     * end upload.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    void endUpload(PortalControllerContext portalControllerContext) throws PortletException;
    
}
