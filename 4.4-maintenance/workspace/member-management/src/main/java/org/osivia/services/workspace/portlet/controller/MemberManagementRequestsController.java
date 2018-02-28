package org.osivia.services.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
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
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("invitationRequests") InvitationRequestsForm form,
            @RequestParam(value = "sort", defaultValue = "date") String sort, @RequestParam(value = "alt", defaultValue = "true") String alt)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tab
        request.setAttribute("tab", "requests");

        // Sort member object
        this.service.sortInvitationRequests(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);

        return "requests/view";
    }


    /**
     * Update invitation requests.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form invitation requests form model attribute
     * @throws PortletException
     */
    @ActionMapping("update")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("invitationRequests") InvitationRequestsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateInvitationRequests(portalControllerContext, options, form);

        // Copy render parameter
        this.copyRenderParameters(request, response);
    }


    /**
     * Copy render parameters.
     *
     * @param request action request
     * @param response action response
     */
    private void copyRenderParameters(ActionRequest request, ActionResponse response) {
        response.setRenderParameter("tab", "requests");

        String sortParameter = request.getParameter("sort");
        if (StringUtils.isNotEmpty(sortParameter)) {
            response.setRenderParameter("sort", sortParameter);
        }

        String altParameter = request.getParameter("alt");
        if (StringUtils.isNotEmpty(altParameter)) {
            response.setRenderParameter("alt", altParameter);
        }
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

}
