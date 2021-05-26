package org.osivia.services.workspace.filebrowser.portlet.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserBulkDownloadContent;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserView;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 * File browser portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class FileBrowserController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private FileBrowserService service;


    /**
     * Constructor.
     */
    public FileBrowserController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @RequestParam(name = "view", required = false) String viewId) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // View
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        request.setAttribute("view", view.getId());

        // Update menubar
        //this.service.updateMenubar(portalControllerContext);

        return "view";
    }


    /**
     * Change view action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param viewId   view identifier request parameter
     * @param form     form model attribute
     */
    @ActionMapping("change-view")
    public void changeView(ActionRequest request, ActionResponse response, @RequestParam("view") String viewId, @ModelAttribute("form") FileBrowserForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // View
        FileBrowserView view = FileBrowserView.fromId(viewId);

        this.service.saveView(portalControllerContext, form, view);

        // Copy view render parameter
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Sort action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param fieldId  sort field request parameter
     * @param viewId   view identifier render parameter
     * @param alt      alternative sort indicator request parameter
     * @param form     form model attribute
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("field") String fieldId, @RequestParam("alt") String alt,
                     @RequestParam(name = "view", required = false) String viewId, @ModelAttribute("form") FileBrowserForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Sort field
        FileBrowserSortField field = this.service.getSortField(portalControllerContext, fieldId);

        // Sort
        this.service.sortItems(portalControllerContext, form, field, BooleanUtils.toBoolean(alt));

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Duplicate document action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param path     document path request parameter
     * @param viewId   view identifier request parameter
     * @param form     form model attribute
     */
    @ActionMapping("duplicate")
    public void duplicate(ActionRequest request, ActionResponse response, @RequestParam("path") String path,
                          @RequestParam(name = "view", required = false) String viewId, @ModelAttribute("form") FileBrowserForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.duplicate(portalControllerContext, form, path);


    }


    /**
     * Drop action mapping.
     *
     * @param request   action request
     * @param response  action response
     * @param sourceIds source identifiers request parameter
     * @param targetId  target identifier request parameter
     * @param viewId    view identifier request parameter
     */
    @ActionMapping("drop")
    public void drop(ActionRequest request, ActionResponse response, @RequestParam("sourceIds") String sourceIds, @RequestParam("targetId") String targetId,
                     @RequestParam(name = "view", required = false) String viewId) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.drop(portalControllerContext, Arrays.asList(StringUtils.split(sourceIds, ",")), targetId);

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Upload action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param viewId   view identifier request parameter
     * @param form     form model attribute
     */
    @ActionMapping("upload")
    public void upload(ActionRequest request, ActionResponse response, @RequestParam(name = "view", required = false) String viewId,
                       @ModelAttribute("form") FileBrowserForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.upload(portalControllerContext, form);

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }

    /**
     * End upload action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param viewId   view identifier request parameter
     */
    @ActionMapping("endUpload")
    public void endUpload(ActionRequest request, ActionResponse response, @RequestParam(name = "view", required = false) String viewId) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.endUpload(portalControllerContext);

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Get toolbar resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param indexes  selected items indexes
     * @param viewId   view identifier request parameter
     * @param form     form model attribute
     */
    @ResourceMapping("toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam(name = "indexes", required = false) String indexes,
                           @RequestParam(name = "view", required = false) String viewId, @ModelAttribute("form") FileBrowserForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getToolbar(portalControllerContext, form, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")),
                viewId);

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }


    /**
     * Get bulk download content resouce mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param paths    document paths
     */
    @ResourceMapping("bulk-download")
    public void getBulkDownload(ResourceRequest request, ResourceResponse response, @RequestParam("paths") String paths) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Bulk download content
        FileBrowserBulkDownloadContent content = this.service.getBulkDownload(portalControllerContext, Arrays.asList(StringUtils.split(paths, ",")));

        // Content type
        response.setContentType(content.getType());
        // Content disposition
        response.setProperty("Content-disposition", content.getDisposition());
        // Character encoding
        response.setCharacterEncoding(CharEncoding.UTF_8);
        // No cache
        response.getCacheControl().setExpirationTime(0);
        // Buffer size
        response.setBufferSize(4096);

        // Input steam
        InputStream inputSteam = new FileInputStream(content.getFile());
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputSteam, outputStream);
        outputStream.close();
    }


    /**
     * Get location resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param path     document path request parameter
     */
    @ResourceMapping("location")
    public void getLocation(ResourceRequest request, ResourceResponse response, @RequestParam("path") String path) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element location = this.service.getLocationBreadcrumb(portalControllerContext, path);

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(location);
        htmlWriter.close();
    }


    /**
     * Get form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public FileBrowserForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("basePath", "path", "listMode", "initialized");
    }


    /**
     * Get views model attribute.
     *
     * @return views
     */
    @ModelAttribute("views")
    public List<FileBrowserView> getViews() {
        return Arrays.asList(FileBrowserView.values());
    }


    /**
     * Get sort fields model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return sort fields
     */
    @ModelAttribute("sortFields")
    public List<FileBrowserSortField> getSortFields(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSortFields(portalControllerContext);
    }

}
