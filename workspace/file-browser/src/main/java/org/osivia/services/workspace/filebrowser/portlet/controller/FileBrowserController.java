package org.osivia.services.workspace.filebrowser.portlet.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserBulkDownloadContent;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserForm;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSort;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserView;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;

/**
 * File browser portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class FileBrowserController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
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
     * @param request render request
     * @param response render response
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @RequestParam(name = "view", required = false) String viewId) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // View
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        request.setAttribute("view", view.getId());

        // Update menubar
        this.service.updateMenubar(portalControllerContext);

        return "view";
    }


    /**
     * Change view action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param viewId view identifier
     * @throws PortletException
     */
    @ActionMapping("change-view")
    public void changeView(ActionRequest request, ActionResponse response, @RequestParam("view") String viewId) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // View
        FileBrowserView view = FileBrowserView.fromId(viewId);

        this.service.saveView(portalControllerContext, view);

        // Copy view render parameter
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Sort action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sort sort request parameter
     * @param viewId view identifier render parameter
     * @param alt alternative sort indicator request parameter
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sort") String sort, @RequestParam("alt") String alt,
            @RequestParam(name = "view", required = false) String viewId, @ModelAttribute("form") FileBrowserForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sortItems(portalControllerContext, form, FileBrowserSort.fromId(sort), BooleanUtils.toBoolean(alt));

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Duplicate document action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param path document path request parameter
     * @param viewId view identifier request parameter
     * @throws PortletException
     */
    @ActionMapping("duplicate")
    public void duplicate(ActionRequest request, ActionResponse response, @RequestParam("path") String path,
            @RequestParam(name = "view", required = false) String viewId) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.duplicate(portalControllerContext, path);

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Delete documents action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param identifiers document identifiers request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String identifiers,
            @RequestParam(name = "view", required = false) String viewId) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, Arrays.asList(StringUtils.split(identifiers, ",")));

        // Copy view render parameter
        FileBrowserView view = this.service.getView(portalControllerContext, viewId);
        response.setRenderParameter("view", view.getId());
    }


    /**
     * Drop action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sourceIds source identifiers request parameter
     * @param targetId target identifier request parameter
     * @param viewId view identifier request parameter
     * @throws PortletException
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
     * @param request action request
     * @param response action response
     * @param viewId view identifier request parameter
     * @param form form model attribute
     * @throws PortletException
     * @throws IOException
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
     * Get toolbar resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param indexes selected items indexes
     * @param viewId view identifier request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam(name = "indexes", required = false) String indexes,
            @RequestParam(name = "view", required = false) String viewId) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getToolbar(portalControllerContext, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")), viewId);

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
     * @param request resource request
     * @param response resource response
     * @param paths document paths
     * @throws PortletException
     * @throws IOException
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
     * Get form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public FileBrowserForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Get views model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return views
     * @throws PortletException
     */
    @ModelAttribute("views")
    public List<FileBrowserView> getViews(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(FileBrowserView.values());
    }


    /**
     * Get sorts model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return sorts
     * @throws PortletException
     */
    @ModelAttribute("sorts")
    public List<FileBrowserSort> getSorts(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(FileBrowserSort.values());
    }

}
