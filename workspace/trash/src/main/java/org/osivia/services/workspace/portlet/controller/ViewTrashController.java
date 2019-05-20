package org.osivia.services.workspace.portlet.controller;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.TrashForm;
import org.osivia.services.workspace.portlet.model.TrashFormSort;
import org.osivia.services.workspace.portlet.service.TrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.annotation.PostConstruct;
import javax.portlet.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * View trash portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
public class ViewTrashController extends CMSPortlet {

    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private TrashService service;


    /**
     * Constructor.
     */
    public ViewTrashController() {
        super();
    }


    /**
     * Post-construct.
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Add menubar items
        this.service.addMenubarItems(portalControllerContext);

        return "view";
    }


    /**
     * Sort action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param sortId   sort property identifier request parameter
     * @param alt      alternative sort indicator request parameter
     * @param form     trash form model attribute
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
                     @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form, TrashFormSort.fromId(sortId), BooleanUtils.toBoolean(alt));
    }


    /**
     * Restore all items action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     trash form model attribute
     */
    @ActionMapping("restore-all")
    public void restoreAll(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restoreAll(portalControllerContext, form);
    }


    /**
     * Empty trash action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     trash form model attribute
     */
    @ActionMapping("delete-all")
    public void emptyTrash(ActionRequest request, ActionResponse response, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.emptyTrash(portalControllerContext, form);
    }


    /**
     * Restore selected items action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form     trash form model attribute
     */
    @ActionMapping("restore")
    public void restore(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restore(portalControllerContext, form, identifiers);
    }


    /**
     * Delete selected items action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param form     trash form model attribute
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers, @ModelAttribute("trashForm") TrashForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form, identifiers);
    }


    /**
     * Get trash form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return trash form
     */
    @ModelAttribute("trashForm")
    public TrashForm getTrashForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getTrashForm(portalControllerContext);
    }


    /**
     * Get toolbar resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param indexes  selected row indexes
     */
    @ResourceMapping("toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam("indexes") String indexes) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getToolbar(portalControllerContext, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")));

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }


    /**
     * Get location breadcrumb resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param path     location path request parameter
     */
    @ResourceMapping("location-breadcrumb")
    public void getLocationBreadcrumb(ResourceRequest request, ResourceResponse response, @RequestParam("path") String path)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Location breadcrumb
        Element breadcrumb = this.service.getLocationBreadcrumb(portalControllerContext, path);

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(breadcrumb);
        htmlWriter.close();
    }

}
