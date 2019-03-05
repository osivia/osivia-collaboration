package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.util.Arrays;

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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.MembersSort;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Workspace member management portlet view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class MemberManagementController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public MemberManagementController() {
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
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Tab
        request.setAttribute("tab", "members");

        return "members/view";
    }


    /**
     * Portlet exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @param exception portlet exception
     * @return error path
     */
    @ExceptionHandler(PortletException.class)
    public String handlePortletException(PortletRequest request, PortletResponse response, PortletException exception) {
        request.setAttribute("exception", exception);
        return "error";
    }


    /**
     * Sort action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sortId sort identifier request parameter
     * @param alt alternative sort indicator request parameter
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
            @ModelAttribute("members") MembersForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sortMembers(portalControllerContext, form, MembersSort.fromId(sortId), BooleanUtils.toBoolean(alt));

        // Copy render parameter
        response.setRenderParameter("tab", "members");
    }


    /**
     * Remove action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param identifiers selected member identifiers request parameter
     * @param options options model attribute
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("remove")
    public void remove(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers,
            @ModelAttribute("options") MemberManagementOptions options, @ModelAttribute("members") MembersForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.removeMembers(portalControllerContext, options, form, identifiers);

        // Copy render parameter
        response.setRenderParameter("tab", "members");
    }


    /**
     * Get options model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public MemberManagementOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get members form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param options options model attribute
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("members")
    public MembersForm getMembersForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getMembersForm(portalControllerContext);
    }


    /**
     * Get help model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return help
     * @throws PortletException
     */
    @ModelAttribute("help")
    public String getHelp(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getMembersHelp(portalControllerContext);
    }


    /**
     * Get members toolbar resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param indexes selected items indexes
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("members-toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam(name = "indexes", required = false) String indexes)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getMembersToolbar(portalControllerContext, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")));

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }


    /**
     * Export members table in CSV format resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param members members form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("export-members-csv")
    public void exportCsv(ResourceRequest request, ResourceResponse response, @ModelAttribute("members") MembersForm members,
            @ModelAttribute("options") MemberManagementOptions options) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Content type
        response.setContentType("text/csv");
        // Content disposition
        response.setProperty("Content-disposition", "attachment; filename=\"" + "members_" + options.getWorkspaceId() + ".csv" + "\"");

        this.service.exportMembersCsv(portalControllerContext, members, response.getPortletOutputStream());
    }

}
