package org.osivia.services.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.ResendInvitationsForm;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Resend invitation portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = {"tab=invitations", "view=resend"})
@SessionAttributes("resendInvitationsForm")
public class MemberManagementResendInvitationsController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private MemberManagementService service;


    /**
     * Constructor.
     */
    public MemberManagementResendInvitationsController() {
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
        request.setAttribute("tab", "invitations");
        request.setAttribute("view", "resend");

        return "resend-invitations/view";
    }


    /**
     * Redirect tab action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param redirection redirection request parameter
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("redirectTab")
    public void redirectTab(ActionRequest request, ActionResponse response, @RequestParam("redirection") String redirection, SessionStatus sessionStatus)
            throws PortletException {
        sessionStatus.setComplete();
        response.setRenderParameter("tab", redirection);
    }


    /**
     * Submit action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @param options options model attribute
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("submit")
    public void submit(ActionRequest request, ActionResponse response, @ModelAttribute("resendInvitationsForm") ResendInvitationsForm form,
            @ModelAttribute("options") MemberManagementOptions options, SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.resendInvitations(portalControllerContext, options, form);

        sessionStatus.setComplete();
        response.setRenderParameter("tab", "invitations");
    }


    /**
     * Get resend invitations form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param identifiers selected invitation identifiers
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("resendInvitationsForm")
    public ResendInvitationsForm getForm(PortletRequest request, PortletResponse response, @RequestParam("identifiers") String[] identifiers)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getResendInvitationsForm(portalControllerContext, identifiers);
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

}
