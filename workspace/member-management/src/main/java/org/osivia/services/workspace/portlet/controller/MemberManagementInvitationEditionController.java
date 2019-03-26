package org.osivia.services.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.converter.LocalGroupPropertyEditor;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Member management invitation edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=invitation-edition")
@SessionAttributes("invitationEditionForm")
public class MemberManagementInvitationEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private MemberManagementService service;

    /** Local group property editor. */
    @Autowired
    private LocalGroupPropertyEditor localGroupPropertyEditor;


    /**
     * Constructor.
     */
    public MemberManagementInvitationEditionController() {
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
        request.setAttribute("view", "invitation-edition");

        return "invitation-edition/view";
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
     * Resend invitation action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form invitation edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "submit", params = "resend")
    public void resend(ActionRequest request, ActionResponse response, @ModelAttribute("invitationEditionForm") InvitationEditionForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.resendInvitation(portalControllerContext, form);

        response.setRenderParameter("view", "invitation-edition");
        response.setRenderParameter("invitationPath", form.getPath());
    }


    /**
     * Save modifications action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form invitation edition form model attribute
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping(name = "submit", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("invitationEditionForm") InvitationEditionForm form,
            SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateInvitation(portalControllerContext, form);

        sessionStatus.setComplete();
        response.setRenderParameter("tab", "invitations");
    }


    /**
     * Cancel modfications action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form invitation edition form model attribute
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping(name = "submit", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("invitationEditionForm") InvitationEditionForm form,
            SessionStatus sessionStatus) throws PortletException {
        sessionStatus.setComplete();
        response.setRenderParameter("tab", "invitations");
    }


    /**
     * Delete invitation action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form invitation edition form model attribute
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("invitationEditionForm") InvitationEditionForm form,
            SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteInvitation(portalControllerContext, form);

        sessionStatus.setComplete();
        response.setRenderParameter("tab", "invitations");
    }


    /**
     * Get invitation edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param path invitation document path request parameter
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("invitationEditionForm")
    public InvitationEditionForm getInvitationEditionForm(PortletRequest request, PortletResponse response,
            @RequestParam(name = "invitationPath", required = false) String path) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getInvitationEditionForm(portalControllerContext, path);
    }


    /**
     * Invitation edition form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("invitationEditionForm")
    public void invitationEditionFormInitBinder(PortletRequestDataBinder binder) {
        binder.registerCustomEditor(CollabProfile.class, this.localGroupPropertyEditor);
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
