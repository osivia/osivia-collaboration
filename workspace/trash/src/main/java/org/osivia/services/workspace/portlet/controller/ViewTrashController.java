package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.TrashForm;
import org.osivia.services.workspace.portlet.service.TrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import net.sf.json.JSONObject;

/**
 * View trash portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class ViewTrashController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet service. */
    @Autowired
    private TrashService service;


    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public ViewTrashController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param form trash form model attribute
     * @param sort sort property request parameter
     * @param alt alternative sort indicator request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("trashForm") TrashForm form,
            @RequestParam(name = "sort", defaultValue = "date") String sort, @RequestParam(name = "alt", defaultValue = "true") String alt)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Sort
        this.service.sort(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);

        // Add menubar items
        this.service.addMenubarItems(portalControllerContext);

        return "view";
    }


    /**
     * Restore all items action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     * @throws PortletException
     */
    @ActionMapping("restore")
    public void restoreAll(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restoreAll(portalControllerContext, form);
    }


    /**
     * Empty trash action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     * @throws PortletException
     */
    @ActionMapping("empty")
    public void emptyTrash(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.emptyTrash(portalControllerContext, form);
    }


    /**
     * Restore selected items action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "update", params = "restore")
    public void restore(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restore(portalControllerContext, form);
    }


    /**
     * Delete selected items action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form trash form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "update", params = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form);
    }


    /**
     * Get trash form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return trash form
     * @throws PortletException
     */
    @ModelAttribute("trashForm")
    public TrashForm getTrashForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getTrashForm(portalControllerContext);
    }


    /**
     * Get toolbar message resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param count selection count request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("toolbar-message")
    public void getToolbarMessage(ResourceRequest request, ResourceResponse response, @RequestParam("count") String count)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Toolbar message
        String message = this.service.getToolbarMessage(portalControllerContext, NumberUtils.toInt(count));


        // JSON
        JSONObject data = new JSONObject();
        data.put("message", message);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data.toString());
        printWriter.close();
    }


    /**
     * Get location breadcrumb resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param path location path request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("location-breadcrumb")
    public void getLocationBreadcrumb(ResourceRequest request, ResourceResponse response, @RequestParam("path") String path)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Location breadcrumb
        Element breadcrumb = this.service.getLocationBreadcrumb(portalControllerContext, path);

        // Content type
        response.setContentType("application/json");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(breadcrumb);
        htmlWriter.close();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
