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
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersSort;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Member management portlet view requests controller.
 *
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping(path = "VIEW", params = "tab=requests")
public class MemberManagementRequestsController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;


    /**
     * Constructor
     */
    public MemberManagementRequestsController() {
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
        request.setAttribute("tab", "requests");

        return "requests/view";
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
            @ModelAttribute("invitationRequests") InvitationRequestsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sortInvitationRequests(portalControllerContext, form, MembersSort.fromId(sortId), BooleanUtils.toBoolean(alt));

        // Copy render parameter
        response.setRenderParameter("tab", "requests");
    }


    /**
     * Accept invitation requests action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param identifiers selected invitation request identifiers request parameter
     * @param options options model attribute
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("accept")
    public void accept(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers,
            @ModelAttribute("options") MemberManagementOptions options, @ModelAttribute("invitationRequests") InvitationRequestsForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.acceptInvitationRequests(portalControllerContext, options, form, identifiers);

        // Copy render parameter
        response.setRenderParameter("tab", "requests");
    }


    /**
     * Decline invitation requests action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param identifiers selected invitation request identifiers request parameter
     * @param options options model attribute
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("decline")
    public void decline(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers,
            @ModelAttribute("options") MemberManagementOptions options, @ModelAttribute("invitationRequests") InvitationRequestsForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.declineInvitationRequests(portalControllerContext, options, form, identifiers);

        // Copy render parameter
        response.setRenderParameter("tab", "requests");
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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get invitation request form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("invitationRequests")
    public InvitationRequestsForm getInvitationRequestsForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getInvitationRequestsForm(portalControllerContext);
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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getInvitationRequestsHelp(portalControllerContext);
    }


    /**
     * Get invitation requests toolbar resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param indexes selected items indexes
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("invitation-requests-toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam(name = "indexes", required = false) String indexes)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getInvitationRequestsToolbar(portalControllerContext,
                Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")));

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }

}
